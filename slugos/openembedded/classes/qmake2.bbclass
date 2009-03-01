#
# QMake variables for Qt4
#
inherit qmake_base

DEPENDS_prepend = "qmake2-native uicmoc4-native "

export QMAKESPEC = "${CROSS_DATADIR}/qt4/mkspecs/${TARGET_OS}-oe-g++"
export OE_QMAKE_UIC = "${STAGING_BINDIR_NATIVE}/uic4"
export OE_QMAKE_UIC3 = "${STAGING_BINDIR_NATIVE}/uic34"
export OE_QMAKE_MOC = "${STAGING_BINDIR_NATIVE}/moc4"
export OE_QMAKE_RCC = "${STAGING_BINDIR_NATIVE}/rcc4"
export QMAKE_RCC = "${STAGING_BINDIR_NATIVE}/rcc4"
export OE_QMAKE_QMAKE = "${STAGING_BINDIR_NATIVE}/qmake2"
export OE_QMAKE_LINK = "${CXX}"
export OE_QMAKE_CXXFLAGS = "${CXXFLAGS}"
export OE_QMAKE_INCDIR_QT = "${STAGING_INCDIR}/qt4"
export OE_QMAKE_LIBDIR_QT = "${STAGING_LIBDIR}"
export OE_QMAKE_LIBS_QT = "qt"
export OE_QMAKE_LIBS_X11 = "-lXext -lX11 -lm"
