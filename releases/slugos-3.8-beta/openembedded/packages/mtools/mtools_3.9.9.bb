# mtools OE build file
# Copyright (C) 2004-2006, Advanced Micro Devices, Inc.  All Rights Reserved
# Released under the MIT license (see packages/COPYING)

DESCRIPTION="Mtools is a collection of utilities for accessing MS-DOS disks from Unix without mounting them."
HOMEPAGE="http://mtools.linux.lu"
MAINTAINER = "Raymond Danks <info-linux@geode.amd.com>"
LICENSE="GPL"

SRC_URI="http://mtools.linux.lu/mtools-${PV}.tar.gz \
	file://mtools-makeinfo.patch;patch=1"

#DEPENDS = "tetex-native"

inherit autotools
