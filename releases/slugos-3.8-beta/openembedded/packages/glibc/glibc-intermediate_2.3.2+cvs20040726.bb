SECTION = "libs"
include glibc_${PV}.bb

DEFAULT_PREFERENCE_sh3 = "-99"

do_install () {
	:
}

PACKAGES = ""
PROVIDES = "virtual/${TARGET_PREFIX}libc-for-gcc"
DEPENDS += " virtual/${TARGET_PREFIX}gcc-initial "
GLIBC_ADDONS = "linuxthreads"
GLIBC_EXTRA_OECONF = ""
