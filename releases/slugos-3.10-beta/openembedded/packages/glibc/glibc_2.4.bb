DESCRIPTION = "GNU C Library"
HOMEPAGE = "http://www.gnu.org/software/libc/libc.html"
LICENSE = "LGPL"
SECTION = "libs"
PRIORITY = "required"
DEFAULT_PREFERENCE = "-1"
PR = "r5"

# the -isystem in bitbake.conf screws up glibc do_stage
BUILD_CPPFLAGS = "-I${STAGING_DIR}/${BUILD_SYS}/include"
TARGET_CPPFLAGS = "-I${STAGING_DIR}/${TARGET_SYS}/include"


FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/glibc-2.4"

GLIBC_ADDONS ?= "ports,nptl,libidn"
GLIBC_EXTRA_OECONF ?= ""

GLIBC_BROKEN_LOCALES = "sid_ET tr_TR mn_MN"

#
# For now, we will skip building of a gcc package if it is a uclibc one
# and our build is not a uclibc one, and we skip a glibc one if our build
# is a uclibc build.
#
# See the note in gcc/gcc_3.4.0.oe
#

python __anonymous () {
    import bb, re
    uc_os = (re.match('.*uclibc$', bb.data.getVar('TARGET_OS', d, 1)) != None)
    if uc_os:
        raise bb.parse.SkipPackage("incompatible with target %s" %
                                   bb.data.getVar('TARGET_OS', d, 1))
}

# nptl needs unwind support in gcc, which can't be built without glibc.
PROVIDES = "virtual/libc ${@['virtual/${TARGET_PREFIX}libc-for-gcc', '']['nptl' in '${GLIBC_ADDONS}']}"
PROVIDES += "virtual/libintl virtual/libiconv"
DEPENDS = "${@['virtual/${TARGET_PREFIX}gcc-initial', 'virtual/${TARGET_PREFIX}gcc']['nptl' in '${GLIBC_ADDONS}']} linux-libc-headers"
RDEPENDS_${PN}-dev = "linux-libc-headers-dev"
INHIBIT_DEFAULT_DEPS = "1"

#	   file://noinfo.patch;patch=1
#	   file://ldconfig.patch;patch=1;pnum=0
#	   file://arm-machine-gmon.patch;patch=1;pnum=0 \
#	   \
#	   file://arm-ioperm.patch;patch=1;pnum=0 \
#	   file://ldd.patch;patch=1;pnum=0 \
SRC_URI = "ftp://ftp.gnu.org/pub/gnu/glibc/glibc-2.4.tar.bz2 \
	   ftp://ftp.gnu.org/pub/gnu/glibc/glibc-ports-2.4.tar.bz2 \
	   ftp://ftp.gnu.org/pub/gnu/glibc/glibc-libidn-2.4.tar.bz2 \
           file://arm-memcpy.patch;patch=1 \
           file://arm-longlong.patch;patch=1 \
           file://fhs-linux-paths.patch;patch=1 \
           file://dl-cache-libcmp.patch;patch=1 \
           file://ldsocache-varrun.patch;patch=1 \
           file://nptl-crosscompile.patch;patch=1 \
	   file://glibc-2.4-compile.patch;patch=1 \
	   file://fixup-aeabi-syscalls.patch;patch=1 \
	   file://generic-bits_select.h \
	   file://generic-bits_types.h \
	   file://generic-bits_typesizes.h \
	   file://generic-bits_time.h \
           file://etc/ld.so.conf \
           file://generate-supported.mk"

S = "${WORKDIR}/glibc-2.4"
B = "${WORKDIR}/build-${TARGET_SYS}"

inherit autotools

EXTRA_OECONF = "--enable-kernel=${OLDEST_KERNEL} \
	        --without-cvs --disable-profile --disable-debug --without-gd \
		--enable-clocale=gnu \
	        --enable-add-ons=${GLIBC_ADDONS} \
		--with-headers=${STAGING_INCDIR} \
		--without-selinux \
		${GLIBC_EXTRA_OECONF}"

EXTRA_OECONF += "${@get_glibc_fpu_setting(bb, d)}"

def get_glibc_fpu_setting(bb, d):
	if bb.data.getVar('TARGET_FPU', d, 1) in [ 'soft' ]:
		return "--without-fp"
	return ""

do_munge() {
	# Integrate ports and libidn into tree
	mv ${WORKDIR}/glibc-ports-${PV} ${S}/ports
	mv ${WORKDIR}/glibc-libidn-${PV} ${S}/libidn

	# Ports isn't really working... Fix it
	# Some of this is rather dirty, but it seems to be the only
	# quick way to get this cruft to compile
	rm -rf ${S}/ports/sysdeps/unix/sysv/linux/arm/linuxthreads
	ln -s nptl ${S}/ports/sysdeps/unix/sysv/linux/arm/linuxthreads
	cp ${S}/sysdeps/unix/sysv/linux/i386/bits/wchar.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	cp ${S}/sysdeps/wordsize-32/bits/wordsize.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	cp ${WORKDIR}/generic-bits_select.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/select.h
	cp ${WORKDIR}/generic-bits_types.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/types.h
	cp ${WORKDIR}/generic-bits_typesizes.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/typesizes.h
	cp ${WORKDIR}/generic-bits_time.h ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/time.h
	# Copy in generic stuff for not yet implemented headers
	for i in ${S}/bits/*.h; do
		F=`basename $i`
		[ "$F" = "local_lim.h" ] && continue
		[ "$F" = "errno.h" ] && continue
		test -e ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/$F || test -e ${S}/ports/sysdeps/arm/bits/$F || test -e ${S}/sysdeps/unix/sysv/linux/bits/$F || test -e ${S}/sysdeps/ieee754/bits/$F || cp $i ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/
	done
	# This is harmful; we need to get the one from nptl/sysdeps/pthreads
	rm -f ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/libc-lock.h
	# Obsoleted by sysdeps/arm/{fpu,eabi}/bits/fenv.h
	rm -f ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/fenv.h
	# Obsoleted by sysdeps/gnu/bits/utmp.h
	rm -f ${S}/ports/sysdeps/unix/sysv/linux/arm/bits/utmp.h

	# http://www.handhelds.org/hypermail/oe/51/5135.html
	# Some files were moved around between directories on
	# 2005-12-21, which means that any attempt to check out
	# from CVS using a datestamp older than that will be doomed.
	#
	# This is a workaround for that problem.
	rm -rf ${S}/bits
}

addtask munge before do_patch after do_unpack

do_configure () {
# override this function to avoid the autoconf/automake/aclocal/autoheader
# calls for now
# don't pass CPPFLAGS into configure, since it upsets the kernel-headers
# version check and doesn't really help with anything
	if [ -z "`which rpcgen`" ]; then
		echo "rpcgen not found.  Install glibc-devel."
		exit 1
	fi
	(cd ${S} && gnu-configize) || die "failure in running gnu-configize"
	CPPFLAGS="" oe_runconf
}

rpcsvc = "bootparam_prot.x nlm_prot.x rstat.x \
	  yppasswd.x klm_prot.x rex.x sm_inter.x mount.x \
	  rusers.x spray.x nfs_prot.x rquota.x key_prot.x"

do_compile () {
	# -Wl,-rpath-link <staging>/lib in LDFLAGS can cause breakage if another glibc is in staging
	unset LDFLAGS
	base_do_compile
	(
		cd ${S}/sunrpc/rpcsvc
		for r in ${rpcsvc}; do
			h=`echo $r|sed -e's,\.x$,.h,'`
			rpcgen -h $r -o $h || oewarn "unable to generate header for $r"
		done
	)
}

do_stage() {
	rm -f ${STAGING_LIBDIR}/libc.so.6
	oe_runmake 'install_root=${STAGING_DIR}/${HOST_SYS}' \
		   'includedir=/include' 'libdir=/lib' 'slibdir=/lib' \
		   '${STAGING_LIBDIR}/libc.so.6' \
		   install-headers install-lib

	install -d ${STAGING_INCDIR}/gnu \
		   ${STAGING_INCDIR}/bits \
		   ${STAGING_INCDIR}/rpcsvc
	install -m 0644 ${S}/include/gnu/stubs.h ${STAGING_INCDIR}/gnu/
	install -m 0644 ${B}/bits/stdio_lim.h ${STAGING_INCDIR}/bits/
	install -m 0644 misc/syscall-list.h ${STAGING_INCDIR}/bits/syscall.h
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${STAGING_INCDIR}/rpcsvc/
	done
	for i in libc.a libc_pic.a libc_nonshared.a; do
		install -m 0644 ${B}/$i ${STAGING_LIBDIR}/ || die "failed to install $i"
	done
	echo 'GROUP ( libpthread.so.0 libpthread_nonshared.a )' > ${STAGING_LIBDIR}/libpthread.so
	echo 'GROUP ( libc.so.6 libc_nonshared.a )' > ${STAGING_LIBDIR}/libc.so

	rm -f ${CROSS_DIR}/${TARGET_SYS}/lib/libc.so.6
	oe_runmake 'install_root=${CROSS_DIR}/${TARGET_SYS}' \
		   'includedir=/include' 'libdir=/lib' 'slibdir=/lib' \
		   '${CROSS_DIR}/${TARGET_SYS}/lib/libc.so.6' \
		   install-headers install-lib

	install -d ${CROSS_DIR}/${TARGET_SYS}/include/gnu \
		   ${CROSS_DIR}/${TARGET_SYS}/include/bits \
		   ${CROSS_DIR}/${TARGET_SYS}/include/rpcsvc
	install -m 0644 ${S}/include/gnu/stubs.h ${CROSS_DIR}/${TARGET_SYS}/include/gnu/
	install -m 0644 ${B}/bits/stdio_lim.h ${CROSS_DIR}/${TARGET_SYS}/include/bits/
	install -m 0644 misc/syscall-list.h ${CROSS_DIR}/${TARGET_SYS}/include/bits/syscall.h
	for r in ${rpcsvc}; do
		h=`echo $r|sed -e's,\.x$,.h,'`
		install -m 0644 ${S}/sunrpc/rpcsvc/$h ${CROSS_DIR}/${TARGET_SYS}/include/rpcsvc/
	done

	for i in libc.a libc_pic.a libc_nonshared.a; do
		install -m 0644 ${B}/$i ${CROSS_DIR}/${TARGET_SYS}/lib/ || die "failed to install $i"
	done
	echo 'GROUP ( libpthread.so.0 libpthread_nonshared.a )' > ${CROSS_DIR}/${TARGET_SYS}/lib/libpthread.so
	echo 'GROUP ( libc.so.6 libc_nonshared.a )' > ${CROSS_DIR}/${TARGET_SYS}/lib/libc.so
}

include glibc-package.bbclass
