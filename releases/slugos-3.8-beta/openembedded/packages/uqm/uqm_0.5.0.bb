DESCRIPTION = "Star Control 2 source port using SDL (see sc2.sourceforge.net)"
SECTION = "games"
PRIORITY = "optional"
DEPENDS = "virtual/libsdl libsdl-image libsdl-net libvorbis libogg zlib"
SECTION = "games"
PRIORITY = "optional"
MAINTAINER = "Paul Eggleton <paule@handhelds.org>"
LICENSE = "GPL"

PR = "r0"

S = "${WORKDIR}/uqm-${PV}"

SRC_URI = "${SOURCEFORGE_MIRROR}/sc2/uqm-${PV}-source.tar.gz \
           file://build-opts.sh \
"

do_configure() {
	install ${WORKDIR}/build-opts.sh ${S}/
	./build-opts.sh ${STAGING_DIR} ${STAGING_BINDIR} ${STAGING_LIBDIR}
}

do_compile() {
	export ARCH="${TARGET_ARCH}"
	export CC="${CC}"
	export STAGING_INCDIR="${STAGING_INCDIR}"
	export STAGING_LIBDIR="${STAGING_LIBDIR}"
	./build.sh uqm
}

do_install() {
        install -d ${D}${bindir}
        install -m 0755 uqm ${D}${bindir}
}
