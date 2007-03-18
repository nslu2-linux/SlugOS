DESCRIPTION = "A flexible VOIP soft switch/PBX."
HOMEPAGE = "http://www.openpbx.org"
#RDEPENDS = "ssmtp"
SECTION = "voip"
LICENSE = "GPL"
DEPENDS = "openssl zlib tiff libcap spandsp speex readline js"
DEPENDS_${PN}-ldap = "openldap"
RRECOMMENDS = "logrotate"
RRECOMMENDS_${PN}-ogi = "perl perl-module-strict"
PN = "openpbx.org"
PV = "1.2_rc3"
PR = "r0"

SRC_URI = "http://www.openpbx.org/releases/${P}.tar.gz \
           file://bootstrap.patch;patch=1 \
           file://openssl.m4.patch;patch=1 \
           file://logrotate \
           file://volatiles \
           file://init"

PARALLEL_MAKE = ""
INITSCRIPT_NAME = "openpbx"
INITSCRIPT_PARAMS = "defaults 60"

inherit autotools update-rc.d

EXTRA_OECONF = " --with-ssl=${STAGING_DIR}/${HOST_SYS} --enable-low_memory \
        --disable-zaptel --with-directory-layout=lsb --with-chan_fax \
        --with-codec-speex=${STAGING_DIR}/${HOST_SYS} --with-app_ldap \
        --with-perl-shebang='#!${bindir}/perl' --with-jabber --with-res_jabber \
        --enable-t38 --with-javascript --with-res_js \
        --bindir=${bindir} --datadir=${datadir} --sysconfdir=${sysconfdir} \
        --includedir=${includedir} --infodir=${infodir} --mandir=${mandir} \
        --localstatedir=${localstatedir} --libdir=${libdir}"

do_configure_prepend () {
    ${S}/bootstrap.sh
    # Fix some stupidness with the VoiceMail app naming. Case Matters!
    sed -i 's:Voicemail:VoiceMail:' ${S}/configs/extensions.conf.sample
    sed -i 's:/var:${localstatedir}:' ${WORKDIR}/volatiles
    sed -i 's:/var:${localstatedir}:' ${WORKDIR}/logrotate
    sed -i 's:/etc/init.d:${sysconfdir}/init.d:' ${WORKDIR}/logrotate
}

do_install_append() {
    install -c -D -m 755 ${WORKDIR}/init ${D}${sysconfdir}/init.d/openpbx
    install -c -D -m 644 ${WORKDIR}/logrotate ${D}${sysconfdir}/logrotate.d/openpbx
    install -c -D -m 644 ${WORKDIR}/volatiles ${D}${sysconfdir}/default/volatiles/openpbx
}

PACKAGES =+ "${PN}-fax ${PN}-ogi ${PN}-musiconhold ${PN}-ldap"

FILES_${PN}-fax = "${libdir}/openpbx.org/modules/chan_fax.* \
                   ${libdir}/openpbx.org/modules/app_rxfax.* \
                   ${libdir}/openpbx.org/modules/app_txfax.* \
                   ${sysconfdir}/openpbx.org/chan_fax.conf"
FILES_${PN}-musiconhold = "${libdir}/openpbx.org/modules/res_musiconhold.* \
                   ${sysconfdir}/openpbx.org/musiconhold.conf"
FILES_${PN}-ogi = "${libdir}/openpbx.org/modules/res_ogi.* \
                   ${datadir}/openpbx.org/ogi/*"
FILES_${PN}-ldap = "${libdir}/openpbx.org/modules/app_ldap.*"

pkg_postinst_prepend() {
    grep -q openpbx ${sysconfdir}/group || addgroup --system openpbx
    grep -q openpbx ${sysconfdir}/passwd || adduser --system --home ${localstatedir}/run/openpbx.org --no-create-home --disabled-password --ingroup openpbx -s ${base_bindir}/false openpbx
    chown -R openpbx:openpbx ${localstatedir}/lib/openpbx.org ${localstatedir}/spool/openpbx.org ${localstatedir}/log/openpbx.org ${localstatedir}/run/openpbx.org ${sysconfdir}/openpbx.org ${datadir}/openpbx.org
    /etc/init.d/populate-volatile.sh update
}

CONFFILES_${PN}-fax += "${sysconfdir}/openpbx.org/chan_fax.conf"
CONFFILES_${PN}-musiconhold += "${sysconfdir}/openpbx.org/musiconhold.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/adsi.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/adtranvofr.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/agents.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/cdr.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/cdr_custom.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/cdr_manager.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/cdr_tds.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/codecs.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/dnsmgr.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/dundi.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/enum.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/extconfig.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/extensions.ael"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/features.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/iax.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/indications.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/logger.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/manager.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/meetme.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/mgcp.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/modem.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/modules.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/muted.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/openpbx.adsi"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/openpbx.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/osp.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/privacy.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/queues.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/rpt.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/rtp.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/sip.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/sip_notify.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/udptl.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/voicemail.conf"
CONFFILES_${PN} += "${sysconfdir}/openpbx.org/woomera.conf"