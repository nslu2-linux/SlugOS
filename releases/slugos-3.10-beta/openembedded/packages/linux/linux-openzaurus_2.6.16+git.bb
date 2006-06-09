include linux-openzaurus.inc

PR = "r15"

DEFAULT_PREFERENCE = "-1"

# Handy URLs
# git://rsync.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git \
# http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.14.tar.gz \
# http://www.kernel.org/pub/linux/kernel/v2.6/testing/patch-2.6.15-rc1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/v2.6/snapshots/patch-2.6.15-rc2-git1.bz2;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/alan/linux-2.6/2.6.10/patch-2.6.10-ac8.gz;patch=1 \
# http://www.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1 \	   

# Patches submitted upstream are towards top of this list 
# Hacks should clearly named and at the bottom
#           ftp://ftp.kernel.org/pub/linux/kernel/people/akpm/patches/2.6/2.6.14-rc2/2.6.14-rc2-mm1/2.6.14-rc2-mm1.bz2;patch=1 \	   
SRC_URI = "git://rsync.kernel.org/pub/scm/linux/kernel/git/torvalds/linux-2.6.git;tag=cbdf811c77cf2906a099b8da92e9f6f335b68a73 \
           file://rmk-mmc1.patch;patch=1 \
           file://rmk-mmc2.patch;patch=1 \
           ${RPSRC}/mmc_oops_fix-r0.patch;patch=1 \
           ${RPSRC}/mmcsd_large_cards-r0.patch;patch=1 \
           ${RPSRC}/alsa/asoc-v0.10rc4.patch;patch=1 \
           ${RPSRC}/hx2750_base-r24.patch;patch=1 \
           ${RPSRC}/hx2750_bl-r5.patch;patch=1 \
           ${RPSRC}/hx2750_pcmcia-r2.patch;patch=1 \
           ${RPSRC}/pxa_keys-r5.patch;patch=1 \
           ${RPSRC}/tsc2101-r12.patch;patch=1 \
           ${RPSRC}/hx2750_test1-r3.patch;patch=1 \
           ${RPSRC}/pxa_timerfix-r0.patch;patch=1 \
           ${RPSRC}/input_power-r5.patch;patch=1 \
           ${RPSRC}/jffs2_longfilename-r1.patch;patch=1 \
           ${RPSRC}/pxa25x_cpufreq-r0.patch;patch=1 \
           ${RPSRC}/poodle_partsize-r0.patch;patch=1 \
           ${RPSRC}/collie_frontlight-r4.patch;patch=1 \
           ${RPSRC}/zlib_inflate-r3.patch;patch=1 \
           ${RPSRC}/zaurus_reboot-r1.patch;patch=1 \
           ${RPSRC}/sharpsl_pm_fixes1-r0.patch;patch=1 \
           ${RPSRC}/asoc_fixups-r2.patch;patch=1 \
           ${RPSRC}/pm_changes-r1.patch;patch=1 \
           ${RPSRC}/input_switch_rename-r0.patch;patch=1 \
           ${RPSRC}/led_class_kconfig-r0.patch;patch=1 \
           ${RPSRC}/led_maintainer-r0.patch;patch=1 \
           ${RPSRC}/led_sysfs_fix-r0.patch;patch=1 \
           ${RPSRC}/backlight_sysfs_fix-r0.patch;patch=1 \
           ${RPSRC}/sharpsl_pm-do-r2.patch;patch=1 \
           ${RPSRC}/usb_pxa27x_udc-r0.patch;patch=1 \
           ${RPSRC}/usb_add_epalloc-r1.patch;patch=1 \
           ${DOSRC}/kexec-arm-r2.patch;patch=1 \
           ${RPSRC}/pxa_cf_initorder_hack-r1.patch;patch=1 \
           ${RPSRC}/mmcsd_no_scr_check-r0.patch;patch=1 \
           ${RPSRC}/poodle_ts_hack-r0.patch;patch=1 \
           ${RPSRC}/integrator_rgb-r0.patch;patch=1 \
           file://add-oz-release-string.patch;patch=1 \
           file://pxa-serial-hack.patch;patch=1 \
           ${RPSRC}/pxa-linking-bug.patch;patch=1 \
           file://serial-add-support-for-non-standard-xtals-to-16c950-driver.patch;patch=1 \
           file://connectplus-remove-ide-HACK.patch;patch=1 \
           file://24-hostap_cs_id.diff;patch=1 \
           file://hrw-pcmcia-ids-r2.patch;patch=1 \
           file://defconfig-c7x0 \
           file://defconfig-ipaq-pxa270 \
           file://defconfig-collie \
           file://defconfig-poodle \
           file://defconfig-akita \
           file://defconfig-spitz \
           file://defconfig-qemuarm \
           file://defconfig-tosa "
	   
#           http://tglx.de/projects/armirq/2.6.17-rc3/patch-2.6.17-rc3-armirq4.patch;patch=1 \
#           ${RPSRC}/../pxa27x_overlay-r0.patch;patch=1 \

# These patches would really help collie/poodle but we 
# need someone to maintain them
# ${JLSRC}/zaurus-lcd-2.6.11.diff.gz;patch=1 
#   (Pavel Machek's git tree has updated versions of this?)
#   Also parts were recently committed to mainline by rmk (drivers/mfd/)
# ${JLSRC}/zaurus-base-2.6.11.diff.gz;patch=1 
#   (This is mostly in mainline now?)
# ${JLSRC}/zaurus-local-2.6.11.diff.gz;patch=1 \
# ${JLSRC}/zaurus-leds-2.6.11.diff.gz;patch=1 \

SRC_URI_append_tosa = "\
	   ${CHSRC}/usb-ohci-hooks-r1.patch;patch=1 \
	   ${CHSRC}/tmio-core-r4.patch;patch=1 \
	   ${CHSRC}/tmio-tc6393-r5.patch;patch=1 \
	   ${CHSRC}/tmio-nand-r5.patch;patch=1 \
	   ${CHSRC}/tmio-ohci-r3.patch;patch=1 \
	   ${CHSRC}/tmio-fb-r6.patch;patch=1 \
	   ${DOSRC}/tosa-keyboard-r14.patch;patch=1 \
	   ${DOSRC}/tosa-pxaac97-r6.patch;patch=1 \
	   ${DOSRC}/tosa-tmio-r6.patch;patch=1 \
	   ${DOSRC}/tosa-power-r15.patch;patch=1 \
	   ${DOSRC}/tosa-tmio-lcd-r7.patch;patch=1 \
	   ${DOSRC}/tosa-bluetooth-r6.patch;patch=1 \
	   ${DOSRC}/wm97xx-lg7-r0.patch;patch=1 \
	   ${DOSRC}/wm9712-suspend-cold-res-r0.patch;patch=1 \
	   ${DOSRC}/sharpsl-pm-postresume-r0.patch;patch=1 \
	   ${DOSRC}/wm97xx-dig-restore-r0.patch;patch=1 \
	   ${DOSRC}/wm97xx-miscdevs-resume-r0.patch;patch=1 \
	   ${DOSRC}/wm9712-reset-loop-r0.patch;patch=1 \
	   ${DOSRC}/tosa-asoc-r1.patch;patch=1 "

SRC_URI_append_poodle = "\
	   ${RPSRC}/rp_poodle_hacks-r0.patch;patch=1"

S = "${WORKDIR}/git"

# to get module dependencies working
KERNEL_RELEASE = "2.6.17-rc3"
