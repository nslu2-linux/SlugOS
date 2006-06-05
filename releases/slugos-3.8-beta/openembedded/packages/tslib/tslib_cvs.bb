DESCRIPTION = "tslib is a plugin-based flexible touchscreen access library."
HOMEPAGE = "http://cvs.arm.linux.org.uk/"
AUTHOR = "Russel King. Plugins by Chris Larson et. al."
SECTION = "base"
LICENSE = "LGPL"

PV = "0.0+cvs${SRCDATE}"
PR = "r35"

SRC_URI = "cvs://cvs:@pubcvs.arm.linux.org.uk/mnt/src/cvsroot;module=tslib \
           file://usec_fix.patch;patch=1 \
           file://ts.conf \
           file://ts-2.6.conf \
           file://ts.conf-h3600-2.4 \
           file://ts.conf-simpad-2.4 \
           file://ts.conf-corgi-2.4 \
           file://ts.conf-collie-2.4 \
	   file://tslib.sh"
SRC_URI_append_mnci += " file://devfs.patch;patch=1"
SRC_URI_append_mnci += " file://event1.patch;patch=1"
S = "${WORKDIR}/tslib"

inherit autotools pkgconfig

EXTRA_OECONF        = "--enable-shared"
EXTRA_OECONF_mnci   = "--enable-shared --disable-h3600 --enable-input --disable-corgi --disable-collie --disable-mk712 --disable-arctic2 --disable-ucb1x00 "

do_stage() {
	autotools_stage_all
}

do_install_prepend() {
	install -m 0644 ${WORKDIR}/ts.conf ${S}/etc/ts.conf
}

do_install_append() {
	install -d ${D}${sysconfdir}/profile.d/
	install -m 0755 ${WORKDIR}/tslib.sh ${D}${sysconfdir}/profile.d/
	case ${MACHINE} in
	a780 | e680 | h3600 | h3900 | h1940 | h6300 | h2200 | ipaq-pxa270 | blueangel)
		install -d ${D}${datadir}/tslib
		for f in ts-2.6.conf ts.conf-h3600-2.4; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;
	c7x0 | spitz | akita | tosa )
		install -d ${D}${datadir}/tslib
		for f in ts-2.6.conf ts.conf-corgi-2.4; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;
	collie | poodle )
		install -d ${D}${datadir}/tslib
		for f in ts-2.6.conf ts.conf-collie-2.4; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;

	simpad )
		install -d ${D}${datadir}/tslib
		for f in ts-2.6.conf ts.conf-simpad-2.4; do
			install -m 0644 ${WORKDIR}/$f ${D}${datadir}/tslib/
		done
		rm -f ${D}${sysconfdir}/ts.conf
		;;
	*)
		;;
	esac
}

SRC_URI_OVERRIDES_PACKAGE_ARCH = "0"
CONFFILES_${PN} = "${sysconfdir}/ts.conf"

PACKAGE_ARCH_tslib-conf = "${MACHINE_ARCH}"
PACKAGE_ARCH_mnci = "${MACHINE_ARCH}"

PACKAGES = "tslib-conf libts libts-dev tslib-tests tslib-calibrate"

RDEPENDS_libts = "tslib-conf"

FILES_tslib-conf = "${sysconfdir}/ts.conf ${sysconfdir}/profile.d/tslib.sh ${datadir}/tslib"
FILES_libts = "${libdir}/*.so.* ${libdir}/ts/*.so*"
FILES_libts-dev = "${FILES_tslib-dev}"
FILES_tslib-calibrate += "${bindir}/ts_calibrate"
FILES_tslib-tests = "${bindir}/ts_harvest ${bindir}/ts_print ${bindir}/ts_print_raw ${bindir}/ts_test"
