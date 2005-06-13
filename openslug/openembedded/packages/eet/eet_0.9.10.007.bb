DESCRIPTION = "EET is a tiny library designed to write an \
arbitary set of chunks of data to a file and optionally compress \
each chunk (very much like a zip file) and allow fast \
random-access reading of the file later on."
HOMEPAGE = "http://www.enlightenment.org"
SECTION = "e/libs"
PRIORITY = "optional"
DEPENDS = "zlib jpeg"
PR = "r0"
LICENSE = "BSD"

do_prepsources () {
  make clean distclean || true
}
addtask prepsources after do_fetch before do_unpack

SRC_URI = "http://enlightenment.freedesktop.org/files/eet-${PV}.tar.gz"
S = "${WORKDIR}/eet-${PV}"

inherit autotools pkgconfig binconfig

do_stage () {
  oe_libinstall -C src/lib libeet ${STAGING_LIBDIR}/
  install -m 0644 ${S}/src/lib/Eet.h ${STAGING_INCDIR}/
}

FILES_${PN} = "${libdir}/libeet*.so*"
FILES_${PN}-dev += "${bindir} ${libdir}/pkgconfig"
