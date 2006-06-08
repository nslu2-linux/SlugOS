DESCRIPTION = "X data files"
SECTION = "x11/data"
LICENSE= "MIT-X"

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/data/${PN}-X11R7.0-${PV}.tar.bz2"
S = "${WORKDIR}/${PN}-X11R7.0-${PV}"

inherit autotools pkgconfig

do_stage () {
	autotools_stage_all
}