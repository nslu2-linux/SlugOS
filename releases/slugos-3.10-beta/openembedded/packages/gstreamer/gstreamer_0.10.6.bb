DESCRIPTION = "GStreamer is a multimedia framework for encoding and decoding video and sound. \
It supports a wide range of formats including mp3, ogg, avi, mpeg and quicktime."
SECTION = "multimedia"
PRIORITY = "optional"
LICENSE = "LGPL"
HOMEPAGE = "http://www.gstreamer.net/"
MAINTAINER = "Felix Domke <tmbinc@elitedvb.net>"
DEPENDS = "libxml2 glib-2.0 gettext-native popt"

PR = "r0"
# until we have decided a final naming scheme, 
# don't use this package as a replacement for
# version 0.8
DEFAULT_PREFERENCE = "-1"

inherit autotools pkgconfig

SRC_URI = "http://gstreamer.freedesktop.org/src/gstreamer/gstreamer-${PV}.tar.bz2"
EXTRA_OECONF = "--disable-docs-build --disable-dependency-tracking --with-check=no"

do_stage() {
	oe_runmake install prefix=${STAGING_DIR} \
	       bindir=${STAGING_BINDIR} \
	       includedir=${STAGING_INCDIR} \
	       libdir=${STAGING_LIBDIR} \
	       datadir=${STAGING_DATADIR} \
	       mandir=${STAGING_DIR}/share/man
}

FILES_${PN} += " ${libdir}/gstreamer-0.10/*.so"
FILES_${PN}-dev += " ${libdir}/gstreamer-0.10/*.la ${libdir}/gstreamer-0.10/*.a"
