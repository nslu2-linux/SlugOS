DESCRIPTION = "GPE task manager"
SECTION = "gpe"
LICENSE = "GPL"
DEPENDS = "libgpelaunch libgpewidget"

inherit gpe

SRC_URI += "file://setlocale.patch;patch=1"

