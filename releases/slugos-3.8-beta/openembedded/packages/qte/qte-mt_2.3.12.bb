require qte-common_${PV}.inc
PR = "r1"

EXTRA_OECONF += "-thread"

PACKAGE_ARCH = "${MACHINE_ARCH}"
FILES_${PN} = "${palmqtdir}"