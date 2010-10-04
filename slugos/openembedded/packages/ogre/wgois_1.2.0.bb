DESCRIPTION = "Object Oriented Input System (OIS) is meant to be a cross platform, simple solution for using all kinds of Input Devices."
LICENSE = "zlib"
DEPENDS = "virtual/libx11"

SRC_URI = "${SOURCEFORGE_MIRROR}/wgois/ois_${PV}.tar.gz"

inherit autotools_stage

S = "${WORKDIR}/ois"

FILES_${PN} += "${libdir}/libOIS-1*.so"




