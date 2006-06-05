# Kernel for NSLU2
#
# Increment PR_CONFIG for changes to the nslu2-kernel specific
# defconfig (do *NOT* increment anything in here for changes
# to other kernel configs!)
PR_CONFIG = "0"
#
# Increment the number below (i.e. the digits after PR) when
# making changes within this file or for changes to the patches
# applied to the kernel.
PR = "r0.${PR_CONFIG}"

include nslu2-kernel.inc

# N2K_PATCHES - full list of patches to apply
N2K_PATCHES = "\
	file://90-ixp4xx-pci-le.patch;patch=1 \
	file://10-ixp4xx-copy-from.patch;patch=1 \
	file://10-ixp4xx-le.patch;patch=1 \
	file://10-mtdpart-redboot-fis-byteswap.patch;patch=1 \
	file://15-ixp4xx-writesb-l-w.patch;patch=1 \
	file://18-ixp4xx-io-h-addr.patch;patch=1 \
	file://19-jffs2-force-be.patch;patch=1 \
	file://29-ipv4-route-c-spinlock.patch;patch=1 \
	file://30-i2c-x1205.patch;patch=1 \
	file://50-nslu2-arch.patch;patch=1 \
	file://55-nslu2-rtc.patch;patch=1 \
	file://60-nslu2-beeper.patch;patch=1 \
	file://70-nslu2-io.patch;patch=1 \
	file://75-nslu2-leds.patch;patch=1 \
	file://90-pegasus.patch;patch=1 \
	file://20-timer.patch;patch=1 \
	file://91-maclist.patch;patch=1 \
	file://92-nslu2-maclist.patch;patch=1 \
	file://anonymiser.patch;patch=1 \
"

# These options get added to the kernel command line, only put things
# specific to the bootstrap of *this* kernel in here - DISTRO specfic
# config must be in CMDLINE_ROOT (see the full definition of CMDLINE
# in nslu2-kernel.inc)
CMDLINE_KERNEL_OPTIONS = "x1205.hctosys=1"
