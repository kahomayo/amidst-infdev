package amidst.mojangapi.minecraftinterface;

import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import amidst.clazz.symbolic.SymbolicObject;
import amidst.documentation.Immutable;
import amidst.documentation.NotNull;
import amidst.logging.AmidstLogger;

/**
 * Information about what each supported version is
 */
@Immutable
public enum RecognisedVersion {
	// @formatter:off
	// Make sure UNKNOWN is the first entry, so it is always considered newer than all other versions, since an unknown version is most likely a new snapshot.
	// TODO: stronghold reset on V1_9pre4?
	UNKNOWN,
	_20w51a            ("20w51a",             "btudcs$adcwaptaohabjvcgo$badh[Jaddacjwndcmwqwkafrwmcxi"),                                  // matches the launcher version id: 20w51a
	_20w49a            ("20w49a",             "btfdcd$adchapsaogabjvcgo$badh[Jaddacjwndbxwqwkafrwmcwt"),                                  // matches the launcher version id: 20w49a
	_20w48a            ("20w48a",             "bszdaz$adbdapmaoaabfuwgn$badd[Jaczacfwidatwlwfaflwhcvp"),                                  // matches the launcher version id: 20w48a
	_20w46a            ("20w46a",             "bszdaw$adbaapmaoaabfuwgn$badd[Jaczacfwidaqwlwfaflwhcvm"),                                  // matches the launcher version id: 20w46a
	_20w45a            ("20w45a",             "bstdap$adatapmaoaabguwgn$badd[Jaczacfwjdajwmwgaflwicvf"),                                  // matches the launcher version id: 20w45a
	_1_16_4            ("1.16.4",             "bsacyg$acykapcanqaaxungn$bacu[Jacqabwwacyawdvxafcvzcsw"),                                  // matches the launcher version id: 1.16.4               1.16.4-rc1           1.16.4-pre2
	_1_16_4_pre1       ("1.16.4-pre1",        "brzcyf$acyjapbanpaaxungn$bacu[Jacqabwwacxzwdvxafbvzcsv"),                                  // matches the launcher version id: 1.16.4-pre1
	_20w14infinite     ("20w14infinite",      "bobczsamdakrzetcaay[Jaauaaazycvbkxaclugczmujdafdaeudueacu"),                               // matches the launcher version id: 20w14infinite
	_19w14a            ("19w14a",             "bhacnqahbafuvvqbxo[Jxkwrwpcebdzzbrfricodrcrdzj"),                                          // matches the launcher version id: 19w14a
	_3D_Shareware_v1_34("3D Shareware v1.34", "cnrahbafuvvqbxo[Jxkwrwpcebeazbrfricoercrdzj"),                                             // matches the launcher version id: 3D Shareware v1.34
	_19w11a            ("19w11a",             "bgccmpagyafrvtpzxm[Jxiwpwncdbdbyzrdrgcncrarbzh"),                                          // matches the launcher version id: 19w11a
	_19w08a            ("19w08a",             "bdicjtagoafivmpsxf[Jxbwiwgcbbahysqwqzckgqtquza"),                                          // matches the launcher version id: 19w08a
	_18w47a            ("18w47a",             "afhaecuvphwj[Jwfvrvpcaaxuxwqmqrcheqjqkyd"),                                                // matches the launcher version id: 18w47a
	_18w30a            ("18w30a",             "adsxquaom[Ltc;vo[J[[Jvkuwuubvavixaprpvcckpopp"),                                           // matches the launcher version id: 18w30a
	_18w22a            ("18w22a",             "ahexbtjnvxn[Lsl;ux[J[[Jutufudbtayuwjpapecfpoxoy"),                                         // matches the launcher version id: 18w22a
	_18w21a            ("18w21a",             "ahdxatinuxm[Lsk;uw[J[[Jusueucbtaytwiozpdcfoowox"),                                         // matches the launcher version id: 18w21a
	_18w20b            ("18w20b",             "ahawzthntxl[Lsj;uv[J[[Jurudubbsayqwhoypccevovow"),                                         // matches the launcher version id: 18w20b               18w20a
	_18w14a            ("18w14a",             "aatwttensxe[Lsh;us[J[[Juouatybsarzwdowpabwtotou"),                                         // matches the launcher version id: 18w14a
	_18w10c            ("18w10c",             "aaqwqtbnpxb[Lse;up[J[[Jultxtvbparvwaotoxbwcoqor"),                                         // matches the launcher version id: 18w10c
	_18w10b            ("18w10b",             "aapwqtbnpxb[Lse;up[J[[Jultxtvbparuwaotoxbvvoqor"),                                         // matches the launcher version id: 18w10b               18w10a
	_18w07a            ("18w07a",             "aalwlswniwx[Lsa;uk[J[[Jugtstqbparkvvonorbuwokol"),                                         // matches the launcher version id: 18w07a
	_17w49a            ("17w49a",             "yguqrcmivc[Lqr;sq[J[[Jsmryrwbnaooua"),                                                     // matches the launcher version id: 17w49a
	_17w47a            ("17w47a",             "xtufqwmcur[Lql;sk[J[[Jsgrsrqbl"),                                                          // matches the launcher version id: 17w47a
	_17w45a            ("17w45a",             "wptnpzlkua[Lpo;rn[J[[Jrjqvqtbl"),                                                          // matches the launcher version id: 17w45a
	_17w18a            ("17w18a",             "ttqvojmfri[Lny;ov[J[[Jor"),                                                                // matches the launcher version id: 17w18a
	_17w17a            ("17w17a",             "toqqodlzrd[Lns;op[J[[Jol"),                                                                // matches the launcher version id: 17w17a
	_17w16a            ("17w16a",             "tmqooblxrb[Lnq;on[J[[Joj"),                                                                // matches the launcher version id: 17w16a
	_16w39a            ("16w39a",             "rnotmgkfpg[Llv;ms[J[[Jmo"),                                                                // matches the launcher version id: 16w39a
	_15w43a            ("15w43a",             "qqommakaoz[Llq;mm[J[[Jmi"),                                                                // matches the launcher version id: 15w43a
	_15w34b            ("15w34b",             "qbnzlrjtom[Llk;md[J[[Jlz"),                                                                // matches the launcher version id: 15w34b               15w34a
	_15w33b            ("15w33b",             "pwnvlnjtoi[Llg;lz[J[[Jlv"),                                                                // matches the launcher version id: 15w33b               15w33a
	_15w32a            ("15w32a",             "pgnvlnjt[Llg;lz[J[[Jlv"),                                                                  // matches the launcher version id: 15w32a
	_15w14a            ("15w14a",             "otnvlnju[Llg;lz[J[[Jlv"),                                                                  // matches the launcher version id: 15w14a
	_1_8_2_pre4        ("1.8.2-pre4",         "weuzrdnq[Lqu;sp[J[[Jsa"),                                                                  // matches the launcher version id: 1.8.2-pre4           1.8.2-pre3           1.8.2-pre2           1.8.2-pre1
	_14w33c            ("14w33c",             "wauvrbno[Lqs;sm[J[[Jrx"),                                                                  // matches the launcher version id: 14w33c               14w33b               14w33a
	_14w32d            ("14w32d",             "vzuurano[Lqr;sl[J[[Jrw"),                                                                  // matches the launcher version id: 14w32d               14w32c               14w32b
	_14w32a            ("14w32a",             "vyutqznn[Lqq;sk[J[[Jrv"),                                                                  // matches the launcher version id: 14w32a
	_14w31a            ("14w31a",             "vwurqxnm[Lqo;si[J[[Jrt"),                                                                  // matches the launcher version id: 14w31a
	_14w30c            ("14w30c",             "vqulqunj[Lql;sf[J[[Jrq"),                                                                  // matches the launcher version id: 14w30c
	_14w30b            ("14w30b",             "voujqtnj[Lqk;sd[J[[Jro"),                                                                  // matches the launcher version id: 14w30b               14w30a
	_14w29b            ("14w29b",             "vmuhqrnh[Lqi;sb[J[[Jrm"),                                                                  // matches the launcher version id: 14w29b               14w29a               14w28b
	_14w28a            ("14w28a",             "vkufqpng[Lqg;rz[J[[Jrk"),                                                                  // matches the launcher version id: 14w28a
	_14w27b            ("14w27b",             "tusspjmg[Lpb;qq[J[[Jqb"),                                                                  // matches the launcher version id: 14w27b               14w27a
	_14w26c            ("14w26c",             "tmskpemc[Low;qj[J[[Jpu"),                                                                  // matches the launcher version id: 14w26c
	_14w26b            ("14w26b",             "tlsjpdmb[Lov;qi[J[[Jpt"),                                                                  // matches the launcher version id: 14w26b               14w26a
	_14w25b            ("14w25b",             "tjsipcma[Lou;qh[J[[Jps"),                                                                  // matches the launcher version id: 14w25b               14w25a
	_14w20b            ("14w20b",             "tgscowlt[Lon;qb[J[[Jpm"),                                                                  // matches the launcher version id: 14w20b               14w20a
	_14w19a            ("14w19a",             "tcryoslp[Loj;px[J[[Jpi"),                                                                  // matches the launcher version id: 14w19a
	_14w11b            ("14w11b",             "srrnoilg[Lnz;pn[J[[Joy"),                                                                  // matches the launcher version id: 14w11b               14w11a
	_1_7_6_pre2        ("1.7.6-pre2",         "rfqfmzjz[Lmq;of[J[[Jnp"),                                                                  // matches the launcher version id: 1.7.6-pre2
	_1_7_6_pre1        ("1.7.6-pre1",         "reqemzjz[Lmq;oe[J[[Jnp"),                                                                  // matches the launcher version id: 1.7.6-pre1
	_14w10c            ("14w10c",             "sqrmohlf[Lny;pm[J[[Jox"),                                                                  // matches the launcher version id: 14w10c               14w10b
	_14w10a            ("14w10a",             "sprlohlf[Lny;pl[J[[Jox"),                                                                  // matches the launcher version id: 14w10a
	_14w08a            ("14w08a",             "rsqooklf[Lob;mv[J[[J"),                                                                    // matches the launcher version id: 14w08a               14w07a
	_14w06b            ("14w06b",             "rlqhodkz[Lnu;mo[J[[J"),                                                                    // matches the launcher version id: 14w06b
	_14w06a            ("14w06a",             "rjqfobkx[Lns;mm[J[[J"),                                                                    // matches the launcher version id: 14w06a
	_14w05b            ("14w05b",             "rhqeoakw[Lnr;ml[J[[J"),                                                                    // matches the launcher version id: 14w05b               14w05a
	_14w04b            ("14w04b",             "rcpznvkr[Lnm;mg[J[[J"),                                                                    // matches the launcher version id: 14w04b               14w04a
	_14w03b            ("14w03b",             "qtpqnmkk[Lnd;lx[J[[J"),                                                                    // matches the launcher version id: 14w03b
	_14w03a            ("14w03a",             "qsppnlkj[Lnc;lw[J[[J"),                                                                    // matches the launcher version id: 14w03a
	_13w48a            ("13w48a",             "pyoymvjs[Lmm;lg[J[[J"),                                                                    // matches the launcher version id: 13w48a               13w47e               13w47d               13w47c               13w47b               13w47a
	_1_7_1             ("1.7.1",              "puoumrjo[Lmi;lc[J[[J"),                                                                    // matches the launcher version id: 1.7.1                1.7
	_13w43a            ("13w43a",             "ptotmsjp[Lmj;ld[J[[J"),                                                                    // matches the launcher version id: 13w43a
	_13w42b            ("13w42b",             "psosmrjo[Lmi;lc[J[[J"),                                                                    // matches the launcher version id: 13w42b               13w42a
	_13w41b            ("13w41b",             "pmommljn[Lmc;ky[J[[J"),                                                                    // matches the launcher version id: 13w41b
	_13w41a            ("13w41a",             "ppopmojq[Lmf;lb[J[[J"),                                                                    // matches the launcher version id: 13w41a
	_13w38c            ("13w38c",             "numu[Lkn;jh[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w38c               13w38b
	_1_6               ("1.6",                "mrlr[Ljo;hj[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6                  13w26a               13w25c               13w25b               13w25a               13w24b               13w24a
	_13w23b            ("13w23b",             "mjlj[Ljg;hb[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w23b               13w23a
	_13w22a            ("13w22a",             "mhlh[Lje;gz[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w22a               13w21b               13w21a
	_13w19a            ("13w19a",             "mele[Ljb;gw[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w19a               13w18c               13w18b
	_13w18a            ("13w18a",             "mdld[Ljb;gw[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w18a               13w17a               13w16b               13w16a
	_1_5               ("1.5",                "[Bbdtbdlbavymabdmbfsbdpngngbeoavzbfyawpawpaxlavvbfkaumbjaaxwawqaquavsbjwavqbjobkbla"),     // matches the launcher version id: 1.5
	_1_4_3             ("1.4.3",              "[Baylaydaasoldayebadayhmcazcaspbaiatgatgaubaslazyarfbdhaumathanzasibedasgbdvbegbcubcrkg"), // matches the launcher version id: 1.4.3
	_1_4               ("1.4",                "[Baxfawxaarikoawyayxaxblmaxwarjazbasaasaasvarfaysapzbcaatgasbamtarcbcwarabcobczbbnbbkjx"), // matches the launcher version id: 1.4
	_1_3               ("1.3",                "[Barjarbaikarcataarfjfasaateamuamuanmasvavuanxamvaizawpawiawsavjavght"),                   // matches the launcher version id: 1.3
	_1_16_3            ("1.16.3",             "brxcyd$acyhaozannaaxungn$bacs[Jacoabuwacxxwdvxaezvzcst"),                                  // matches the launcher version id: 1.16.3               1.16.3-rc1           1.16.2               1.16.2-rc2           1.16.2-rc1           1.16.2-pre3          1.16.2-pre2
	_1_16_2_pre1       ("1.16.2-pre1",        "brvcya$acyeaoxanlaavumgn$bacq[Jacmabsvycxuwbvvaexvxcsr"),                                  // matches the launcher version id: 1.16.2-pre1
	_20w30a            ("20w30a",             "brucxw$acyaaoxanlaavumgn$bacq[Jacmabsvycxqwbvvaexvxcso"),                                  // matches the launcher version id: 20w30a
	_20w29a            ("20w29a",             "brvcxx$acybaoxanlaayupgn$bact[Jacpabvwbcxrwevyaeywacsp"),                                  // matches the launcher version id: 20w29a
	_20w28a            ("20w28a",             "buodar$adavarqaqeadrxigm$bafm[Jafiaeoyudalyxyrahrytcvj"),                                  // matches the launcher version id: 20w28a
	_20w27a            ("20w27a",             "bqndap$adatanqamezstjgk$aabn[Jabjaapuvdajuyusadtuucvl"),                                   // matches the launcher version id: 20w27a
	_1_16_1            ("1.16.1",             "bqedae$adaianoamczutlgm$aabp[Jablaaruxczyvauuadsuwcva"),                                   // matches the launcher version id: 1.16.1               1.16                 1.16-rc1             1.16-pre8
	_1_16_pre7         ("1.16-pre7",          "bqedaf$adajanoamczutlgm$aabp[Jablaaruxczzvauuadsuwcvb"),                                   // matches the launcher version id: 1.16-pre7            1.16-pre6
	_1_16_pre5         ("1.16-pre5",          "bqddae$adaiannambzttkgl$aabo[Jabkaaquwczyuzutadruvcva"),                                   // matches the launcher version id: 1.16-pre5
	_1_16_pre4         ("1.16-pre4",          "bqddae$adaiannambzutlgl$aabp[Jablaaruxczyvauuadsuwcva"),                                   // matches the launcher version id: 1.16-pre4            1.16-pre3
	_1_16_pre2         ("1.16-pre2",          "bqadaa$adaeanlalzzutlgl$aabp[Jablaaruxczuvauuadruwcuw"),                                   // matches the launcher version id: 1.16-pre2            1.16-pre1
	_20w22a            ("20w22a",             "bpnczm$aczqanfaltzqtmgl$aabk[Jabgaamutczguwuqadmuscui"),                                   // matches the launcher version id: 20w22a
	_20w21a            ("20w21a",             "bpdczc$aczganaalozntlgl$aabh[Jabdaajaahczbmaacxuqcywutczvczuunuoadhcty"),                  // matches the launcher version id: 20w21a
	_20w20b            ("20w20b",             "bpdczl$aczoanaalozntmabh[Jabdaajaahczbmaacxuqczfutdaddacunuoadicug"),                      // matches the launcher version id: 20w20b               20w20a
	_20w19a            ("20w19a",             "bozczq$acztamwalkzktjabe[Jabaaagaaecxblwacuunczkuqdaidahukuladfcul"),                      // matches the launcher version id: 20w19a
	_20w18a            ("20w18a",             "bowczn$aczqamvaljzktjabe[Jabaaagaaecxbltacuunczhuqdafdaeukuladfcui"),                      // matches the launcher version id: 20w18a
	_20w17a            ("20w17a",             "botczj$aczmamqalezithabc[Jaayaaeaaccvblqacsulczduoczyczxuiujadccue"),                      // matches the launcher version id: 20w17a
	_20w16a            ("20w16a",             "bolcyw$aamiakwzdtdaax[Jaatzzzxcvbliacluhcyqukczjcziueufacv"),                              // matches the launcher version id: 20w16a
	_20w15a            ("20w15a",             "boicye$aamfaktzctcaaw[Jaaszyzwcvblfackugcxyujcyrcyqudueacu"),                              // matches the launcher version id: 20w15a
	_20w14a            ("20w14a",             "bobcxp$aameakszbtbaav[Jaarzxzvcvbkyacjufcxjuicyccybucudact"),                              // matches the launcher version id: 20w14a
	_20w13b            ("20w13b",             "bnwcxfalyakmzbtbaav[Jaarzxzvcvbksaciufcwzuicxscxrucudacq"),                                // matches the launcher version id: 20w13b               20w13a
	_20w12a            ("20w12a",             "bnkcwoalrakfyysyaas[Jaaozuzsctbkgacfuccwiufcxbcxatzuaacn"),                                // matches the launcher version id: 20w12a
	_20w11a            ("20w11a",             "bnccwdaljajxyxsxaar[Jaanztzrcsbjyaceubcvxuecwqcwptytzacm"),                                // matches the launcher version id: 20w11a
	_20w10a            ("20w10a",             "bnccvyalkajyyxsxaar[Jaanztzrcsbjzaceubcvsuecwlcwktytzacn"),                                // matches the launcher version id: 20w10a
	_20w09a            ("20w09a",             "bmbcuuakkaiyxyrzzs[Jzoyuyscsbiyabftdcuotgcvhcvgtatbabo"),                                  // matches the launcher version id: 20w09a
	_20w08a            ("20w08a",             "blycuoakhaivxxryzr[Jznytyrcrbivabetccuitfcvbcvasztaabn"),                                  // matches the launcher version id: 20w08a               20w07a
	_20w06a            ("20w06a",             "bkjcsyakaaioxvrwzp[Jzlyrypcrbhgabctacsstdctlctksxsyabk"),                                  // matches the launcher version id: 20w06a
	_1_15_2            ("1.15.2",             "bkacrpajsaikxurwzo[Jzkyqyocrbgxabbtacrjtdcsccsbsxsyabj"),                                  // matches the launcher version id: 1.15.2               1.15.2-pre2          1.15.2-pre1
	_1_15_1            ("1.15.1",             "bjxcrmajpaiixurwzo[Jzkyqyocrbguabbtacrgtdcrzcrysxsyabj"),                                  // matches the launcher version id: 1.15.1               1.15.1-pre1          1.15                 1.15-pre7            1.15-pre6            1.15-pre5            1.15-pre4            1.15-pre3
	_1_15_pre2         ("1.15-pre2",          "bjxcrlajpaiixurwzo[Jzkyqyocrbguabbtacrftdcrycrxsxsyabj"),                                  // matches the launcher version id: 1.15-pre2
	_1_15_pre1         ("1.15-pre1",          "bjxcrkajpaiixurwzo[Jzkyqyocrbguabbtacretdcrxcrwsxsyabj"),                                  // matches the launcher version id: 1.15-pre1
	_19w46b            ("19w46b",             "bjxcrjajpaiixurwzo[Jzkyqyocrbguabbtacrdtdcrwcrvsxsyabj"),                                  // matches the launcher version id: 19w46b               19w46a
	_19w45b            ("19w45b",             "bjtcreajlaiexrrtzl[Jzhynylcobgqaaysxcqytacrrcrqsusvabg"),                                  // matches the launcher version id: 19w45b               19w45a               19w44a
	_19w42a            ("19w42a",             "bjtcrdajlaiexrrtzl[Jzhynylcobgqaaysxcqxtacrqcrpsusvabg"),                                  // matches the launcher version id: 19w42a
	_19w41a            ("19w41a",             "bjscrcajkaidxrrtzl[Jzhynylcobgpaaysxcqwtacrpcrosusvabg"),                                  // matches the launcher version id: 19w41a
	_19w40a            ("19w40a",             "bjocqtajfahyxorrzi[Jzeykyicobglaavsvcqnsycrgcrfssstabd"),                                  // matches the launcher version id: 19w40a               19w39a
	_19w38b            ("19w38b",             "bjgcqlaiyahrxhrkzb[Jyxydybckbgdaaosocqfsrcqycqxslsmaaw"),                                  // matches the launcher version id: 19w38b               19w38a
	_19w37a            ("19w37a",             "bjbcqfaisahlxareyu[Jyqxwxucebfxaahsislcqssfsgaaq"),                                        // matches the launcher version id: 19w37a               19w36a
	_19w35a            ("19w35a",             "bizcpyaiqahjxareyu[Jyqxwxucebfvaahsislcqlsfsgaaq"),                                        // matches the launcher version id: 19w35a
	_19w34a            ("19w34a",             "bixcpwaiqahjxareyu[Jyqxwxucebfuaahsislcqjsfsgaaq"),                                        // matches the launcher version id: 19w34a
	_1_14_4            ("1.14.4",             "bhvcoqahqagjwbqfxv[Jxrwxwvcebeszirjrmcpdrgrhzr"),                                          // matches the launcher version id: 1.14.4               1.14.4-pre7          1.14.4-pre6          1.14.4-pre5          1.14.4-pre4
	_1_14_4_pre3       ("1.14.4-pre3",        "bhucopahpagiwaqexu[Jxqwwwuceberzhrirlcpcrfrgzq"),                                          // matches the launcher version id: 1.14.4-pre3
	_1_14_4_pre2       ("1.14.4-pre2",        "bhtcooahoaghvzqdxt[Jxpwvwtcebeqzgrhrkcpbrerfzp"),                                          // matches the launcher version id: 1.14.4-pre2
	_1_14_4_pre1       ("1.14.4-pre1",        "bhscolahoaghvzqdxt[Jxpwvwtcebepzgrhrkcozrerfzp"),                                          // matches the launcher version id: 1.14.4-pre1
	_1_14_3            ("1.14.3",             "bhqcojahnaggvzqdxt[Jxpwvwtcebenzgrhrkcoxrerfzo"),                                          // matches the launcher version id: 1.14.3               1.14.3-pre4          1.14.3-pre3
	_1_14_3_pre2       ("1.14.3-pre2",        "bhpcoiahnaggvzqdxt[Jxpwvwtcebenzgrhrkcowrerfzo"),                                          // matches the launcher version id: 1.14.3-pre2
	_1_14_3_pre1       ("1.14.3-pre1",        "bhncogahlagevxqcxr[Jxnwtwrcebelzergrjcourdrezm"),                                          // matches the launcher version id: 1.14.3-pre1
	_1_14_2            ("1.14.2",             "bhmcofahkagdvxqcxq[Jxmwtwrcebekzdrgrjcotrdrezl"),                                          // matches the launcher version id: 1.14.2               1.14.2 Pre-Release 4 1.14.2 Pre-Release 3 1.14.2 Pre-Release 2 1.14.2 Pre-Release 1 1.14.1               1.14.1 Pre-Release 2 1.14.1 Pre-Release 1
	_1_14              ("1.14",               "bhlcodahhagavxqcxq[Jxmwtwrcebejzdrgrjcorrdrezl"),                                          // matches the launcher version id: 1.14                 1.14 Pre-Release 5
	_1_14_Pre_Release_4("1.14 Pre-Release 4", "bhjcobahgafzvwqcxp[Jxlwswqcebeizcrgrjcoprdrezk"),                                          // matches the launcher version id: 1.14 Pre-Release 4
	_1_14_Pre_Release_3("1.14 Pre-Release 3", "bhhcnzahfafyvwqcxp[Jxlwswqcebegzcrgrjcomrdrezk"),                                          // matches the launcher version id: 1.14 Pre-Release 3
	_1_14_Pre_Release_2("1.14 Pre-Release 2", "bhecnwahfafyvwqcxp[Jxlwswqcebedzcrgrjcojrdrezk"),                                          // matches the launcher version id: 1.14 Pre-Release 2   1.14 Pre-Release 1
	_19w14b            ("19w14b",             "bhbcntahcafvvwqcxp[Jxlwswqcebeazcrgrjcogrdrezk"),                                          // matches the launcher version id: 19w14b
	_19w13b            ("19w13b",             "bgzcnoahbafuvvqbxo[Jxkwrwpcebdyzbrfricobrcrdzj"),                                          // matches the launcher version id: 19w13b               19w13a
	_19w12b            ("19w12b",             "bgjcmwahaaftvuqaxn[Jxjwqwocebdizarerhcnjrbrczi"),                                          // matches the launcher version id: 19w12b               19w12a
	_19w11b            ("19w11b",             "bgbcmoagyafrvtpzxm[Jxiwpwncdbdayzrdrgcnbrarbzh"),                                          // matches the launcher version id: 19w11b
	_19w09a            ("19w09a",             "bdjcjuagoafivmpsxf[Jxbwiwgcbbaiysqwqzckhqtquza"),                                          // matches the launcher version id: 19w09a               19w08b
	_19w07a            ("19w07a",             "bdfcjpagoafivmprxe[Jxawiwgcbbaeyrqvqzckcqsqtyz"),                                          // matches the launcher version id: 19w07a
	_19w06a            ("19w06a",             "bcycjiaglaffvjpnxc[Jwywfwdcbazxypqsqwcjvqpqqyx"),                                          // matches the launcher version id: 19w06a
	_19w05a            ("19w05a",             "bcvcjfaghafbvkpnwy[Jwuwgwecbaztylqsqwcjsqpqqyt"),                                          // matches the launcher version id: 19w05a
	_19w04b            ("19w04b",             "bcqcjaageaeyvkpnwy[Jwuwgwecbazoylqsqwcjnqpqqyt"),                                          // matches the launcher version id: 19w04b               19w04a
	_19w03c            ("19w03c",             "ageaexvkpmwy[Jwuwgwecbaznylqrqvcjtqoqpyt"),                                                // matches the launcher version id: 19w03c               19w03b               19w03a
	_19w02a            ("19w02a",             "aggaezvmpoxa[Jwwwiwgcbaznynqtqxcjpqqqryv"),                                                // matches the launcher version id: 19w02a
	_18w50a            ("18w50a",             "afyaeqvfpmwt[Jwpwbvzcbayxygqrqvcjaqoqpyo"),                                                // matches the launcher version id: 18w50a
	_18w49a            ("18w49a",             "afnaehuzpjwn[Jwjvvvtcbaybyaqoqschwqlqmyi"),                                                // matches the launcher version id: 18w49a
	_18w48b            ("18w48b",             "aflaeguypiwm[Jwivuvscbaxzxzqnqrchnqkqlyh"),                                                // matches the launcher version id: 18w48b               18w48a
	_18w47b            ("18w47b",             "afiaeduwpiwk[Jwgvsvqcbaxvxxqnqscgwqkqlye"),                                                // matches the launcher version id: 18w47b
	_18w46a            ("18w46a",             "affaeauuphwi[Jwevqvocaaxmxvqmqrcgqqjqkyc"),                                                // matches the launcher version id: 18w46a
	_18w45a            ("18w45a",             "afbadwuspgwg[Jwcvovmcaaxhxtqlqqcfqqiqjya"),                                                // matches the launcher version id: 18w45a
	_18w44a            ("18w44a",             "aeyadtuppewd[Jvzvlvjbyaxcxqqjqncfkqgqhxx"),                                                // matches the launcher version id: 18w44a
	_18w43c            ("18w43c",             "aekyfuopdwc[Jvyvkvibyawjxpqiqmcecqfqgxw"),                                                 // matches the launcher version id: 18w43c
	_18w43b            ("18w43b",             "aekyfuopdwc[Jvyvkvibyawjxpqiqmcebqfqg"),                                                   // matches the launcher version id: 18w43b               18w43a
	_1_13_2            ("1.13.2",             "aduxrubomvp[Jvluxuvbvavlxbprpvcctpopp"),                                                   // matches the launcher version id: 1.13.2               1.13.2-pre2          1.13.2-pre1
	_1_13_1            ("1.13.1",             "aduxrubomvp[Jvluxuvbvavkxbprpvccspopp"),                                                   // matches the launcher version id: 1.13.1               1.13.1-pre2          1.13.1-pre1
	_18w33a            ("18w33a",             "adtxquaolvo[Jvkuwuubvavjxapqpuccrpnpo"),                                                   // matches the launcher version id: 18w33a
	_18w32a            ("18w32a",             "adsxquaolvo[Jvkuwuubvavixapqpuccqpnpo"),                                                   // matches the launcher version id: 18w32a
	_18w31a            ("18w31a",             "aduxsuconvq[Jvmuyuwbvavkxcpspwccqpppq"),                                                   // matches the launcher version id: 18w31a
	_18w30b            ("18w30b",             "adtxruaom[Ltc;vo[J[[Jvkuwuubvavjxaprpvcclpopp"),                                           // matches the launcher version id: 18w30b
	_1_13              ("1.13",               "adrxquaom[Ltc;vo[J[[Jvkuwuubvavhxaprpvccipopp"),                                           // matches the launcher version id: 1.13
	_1_13_pre10        ("1.13-pre10",         "adpxquaom[Ltc;vo[J[[Jvkuwuubvavfxaprpvccgpopp"),                                           // matches the launcher version id: 1.13-pre10
	_1_13_pre9         ("1.13-pre9",          "adoxquaom[Ltc;vo[J[[Jvkuwuubvavexaprpvccgpopp"),                                           // matches the launcher version id: 1.13-pre9            1.13-pre8
	_1_13_pre7         ("1.13-pre7",          "adixquaom[Ltc;vo[J[[Jvkuwuubvauyxaprpvcbxpopp"),                                           // matches the launcher version id: 1.13-pre7
	_1_13_pre6         ("1.13-pre6",          "adexntxoj[Lsz;vl[J[[Jvhuturbvautwxpopscbtplpm"),                                           // matches the launcher version id: 1.13-pre6
	_1_13_pre5         ("1.13-pre5",          "adbxltvoh[Lsx;vj[J[[Jvfurupbvauqwvpmpqcbqpjpk"),                                           // matches the launcher version id: 1.13-pre5
	_1_13_pre4         ("1.13-pre4",          "ahyxntvohya[Lsx;vj[J[[Jvfurupbvazowvpmpqcgnpjpk"),                                         // matches the launcher version id: 1.13-pre4
	_1_13_pre3         ("1.13-pre3",          "ahqxftnnzxs[Lsp;vb[J[[Juxujuhbvazgwnpepicgepbpc"),                                         // matches the launcher version id: 1.13-pre3
	_1_13_pre2         ("1.13-pre2",          "ahixdtlnxxp[Lsn;uz[J[[Juvuhufbvayywlpcpgcfvozpa"),                                         // matches the launcher version id: 1.13-pre2
	_1_13_pre1         ("1.13-pre1",          "ahhxctknwxo[Lsm;uy[J[[Juuuguebuayxwkpbpfcfsoyoz"),                                         // matches the launcher version id: 1.13-pre1
	_18w22c            ("18w22c",             "ahfxctknwxo[Lsm;uy[J[[Juuuguebuayvwkpbpfcfqoyoz"),                                         // matches the launcher version id: 18w22c               18w22b
	_18w21b            ("18w21b",             "ahdxbtjnvxn[Lsl;ux[J[[Jutufudbtaytwjpapecfooxoy"),                                         // matches the launcher version id: 18w21b
	_18w20c            ("18w20c",             "ahcxatinuxm[Lsk;uw[J[[Jusueucbtayswiozpdcexowox"),                                         // matches the launcher version id: 18w20c
	_18w19b            ("18w19b",             "agwwzthntxm[Lsj;uv[J[[Jurudubbsaymwhoypccerovow"),                                         // matches the launcher version id: 18w19b
	_18w19a            ("18w19a",             "afcwytgntxl[Lsi;uu[J[[Juqucuabsawswgoxpbccxouov"),                                         // matches the launcher version id: 18w19a
	_18w16a            ("18w16a",             "aavwutfnsxf[Lsh;ut[J[[Jupubtzbsaskweowpabyhotou"),                                         // matches the launcher version id: 18w16a
	_18w15a            ("18w15a",             "aauwttensxe[Lsh;us[J[[Juouatybsasjwdowpabxrotou"),                                         // matches the launcher version id: 18w15a
	_18w14b            ("18w14b",             "aauwttensxe[Lsh;us[J[[Juouatybsasawdowpabwyotou"),                                         // matches the launcher version id: 18w14b
	_18w11a            ("18w11a",             "aaqwqtbnpxb[Lse;up[J[[Jultxtvbparwwaotoxbwroqor"),                                         // matches the launcher version id: 18w11a
	_18w10d            ("18w10d",             "aaqwqtbnpxb[Lse;up[J[[Jultxtvbparvwaotoxbwmoqor"),                                         // matches the launcher version id: 18w10d
	_18w09a            ("18w09a",             "aakwlswnkww[Lrz;uk[J[[Jugtstqbparovvooosbvkolom"),                                         // matches the launcher version id: 18w09a
	_18w08b            ("18w08b",             "aaiwjsuniwu[Lrx;ui[J[[Juetqtobparmvtomoqbvdojok"),                                         // matches the launcher version id: 18w08b
	_18w08a            ("18w08a",             "aaiwjsuniwu[Lrx;ui[J[[Juetqtobpargvtomoqbuxojok"),                                         // matches the launcher version id: 18w08a
	_18w07c            ("18w07c",             "aahwistniwt[Lrx;uh[J[[Judtptnbparfvsomoqbuhojok"),                                         // matches the launcher version id: 18w07c               18w07b
	_18w06a            ("18w06a",             "aalwlswniwx[Lsa;uk[J[[Jugtstqbpargvvonorbuaokol"),                                         // matches the launcher version id: 18w06a
	_18w05a            ("18w05a",             "znvssdnfwe[Lrs;tr[J[[Jtnszsxbnaqgvcoiombliofog"),                                          // matches the launcher version id: 18w05a
	_18w03b            ("18w03b",             "zjvorznfwa[Lro;tn[J[[Jtjsvstbnaqcuyoibleofog"),                                            // matches the launcher version id: 18w03b               18w03a               18w02a
	_18w01a            ("18w01a",             "zhvnrynevz[Lrn;tm[J[[Jtisussbnapsuxohbkyoeof"),                                            // matches the launcher version id: 18w01a
	_17w50a            ("17w50a",             "ykutremkvf[Lqt;ss[J[[Jsosarybnaovud"),                                                     // matches the launcher version id: 17w50a
	_17w49b            ("17w49b",             "yiusrdmjve[Lqs;sr[J[[Jsnrzrxbnaoquc"),                                                     // matches the launcher version id: 17w49b
	_17w48a            ("17w48a",             "xvugqxmdus[Lqm;sl[J[[Jshrtrrblaoe"),                                                       // matches the launcher version id: 17w48a
	_17w47b            ("17w47b",             "xuufqwmcur[Lql;sk[J[[Jsgrsrqbl"),                                                          // matches the launcher version id: 17w47b
	_17w46a            ("17w46a",             "xiugqslyut[Lqh;sg[J[[Jscrormbl"),                                                          // matches the launcher version id: 17w46a
	_17w45b            ("17w45b",             "wvttqflmug[Lpu;rt[J[[Jrprbqzbl"),                                                          // matches the launcher version id: 17w45b
	_17w43b            ("17w43b",             "vosnozmtta[Loo;qn[J[[Jqjpvpt"),                                                            // matches the launcher version id: 17w43b               17w43a
	_1_12_2            ("1.12.2",             "ulrlozmtry[Loo;pl[J[[Jph"),                                                                // matches the launcher version id: 1.12.2               1.12.2-pre2          1.12.2-pre1          1.12.1               1.12.1-pre1          17w31a
	_1_12              ("1.12",               "ujrjoxmsrw[Lom;pj[J[[Jpf"),                                                                // matches the launcher version id: 1.12                 1.12-pre7            1.12-pre6            1.12-pre5            1.12-pre4            1.12-pre3
	_1_12_pre2         ("1.12-pre2",          "uhrhovmqru[Lok;ph[J[[Jpd"),                                                                // matches the launcher version id: 1.12-pre2
	_1_12_pre1         ("1.12-pre1",          "ugrgoumprt[Loj;pg[J[[Jpc"),                                                                // matches the launcher version id: 1.12-pre1
	_17w18b            ("17w18b",             "tyqyommirl[Lob;oy[J[[Jou"),                                                                // matches the launcher version id: 17w18b
	_17w17b            ("17w17b",             "tpqroemare[Lnt;oq[J[[Jom"),                                                                // matches the launcher version id: 17w17b
	_17w16b            ("17w16b",             "tnqpoclyrc[Lnr;oo[J[[Jok"),                                                                // matches the launcher version id: 17w16b
	_17w15a            ("17w15a",             "tlqnoalwra[Lnp;om[J[[Joi"),                                                                // matches the launcher version id: 17w15a
	_17w14a            ("17w14a",             "tkqmoalwqz[Lnp;om[J[[Joi"),                                                                // matches the launcher version id: 17w14a
	_17w13b            ("17w13b",             "tgqinwlsqv[Lnl;oi[J[[Joe"),                                                                // matches the launcher version id: 17w13b               17w13a
	_17w06a            ("17w06a",             "rsoumhkfph[Llw;mt[J[[Jmp"),                                                                // matches the launcher version id: 17w06a               1.11.2               1.11.1               16w50a
	_1_11              ("1.11",               "rroumhkfph[Llw;mt[J[[Jmp"),                                                                // matches the launcher version id: 1.11                 1.11-pre1
	_16w44a            ("16w44a",             "rqotmgkfpg[Llv;ms[J[[Jmo"),                                                                // matches the launcher version id: 16w44a
	_16w43a            ("16w43a",             "rpotmgkfpg[Llv;ms[J[[Jmo"),                                                                // matches the launcher version id: 16w43a               16w42a               16w41a               16w40a               16w39c               16w39b
	_16w38a            ("16w38a",             "rlosmfkepf[Llu;mr[J[[Jmn"),                                                                // matches the launcher version id: 16w38a
	_16w36a            ("16w36a",             "rkosmfkepf[Llu;mr[J[[Jmn"),                                                                // matches the launcher version id: 16w36a
	_16w35a            ("16w35a",             "rjosmfkepf[Llu;mr[J[[Jmn"),                                                                // matches the launcher version id: 16w35a               16w33a               16w32b               16w32a
	_1_10_2            ("1.10.2",             "rboqmdkcpd[Lls;mp[J[[Jml"),                                                                // matches the launcher version id: 1.10.2               1.10.1               1.10                 1.10-pre2            1.10-pre1
	_16w21b            ("16w21b",             "qzopmckbpc[Llr;mo[J[[Jmk"),                                                                // matches the launcher version id: 16w21b               16w21a
	_16w20a            ("16w20a",             "qxopmckbpc[Llr;mo[J[[Jmk"),                                                                // matches the launcher version id: 16w20a
	_1_9_4             ("1.9.4",              "qwoombkapb[Llq;mn[J[[Jmj"),                                                                // matches the launcher version id: 1.9.4                1.9.3                1.9.3-pre3           1.9.3-pre2           1.9.3-pre1           16w15b
	_16w15a            ("16w15a",             "qwoomajzpb[Llp;mm[J[[Jmi"),                                                                // matches the launcher version id: 16w15a               16w14a               1.RV-Pre1            1.9.2                1.9.1                1.9.1-pre3           1.9.1-pre2           1.9.1-pre1           1.9                  1.9-pre4             1.9-pre3
	_1_9_pre2          ("1.9-pre2",           "qvoomajzpb[Llp;mm[J[[Jmi"),                                                                // matches the launcher version id: 1.9-pre2             1.9-pre1             16w07b               16w07a               16w06a               16w05b               16w05a               16w04a               16w03a               16w02a
	_15w51b            ("15w51b",             "quonmajzpa[Llp;mm[J[[Jmi"),                                                                // matches the launcher version id: 15w51b               15w51a
	_15w50a            ("15w50a",             "qtonmajzpa[Llp;mm[J[[Jmi"),                                                                // matches the launcher version id: 15w50a               15w49b               15w49a               15w47c               15w47b               15w47a
	_15w46a            ("15w46a",             "qsonmajzpa[Llp;mm[J[[Jmi"),                                                                // matches the launcher version id: 15w46a
	_15w45a            ("15w45a",             "qtoombkapb[Llq;mn[J[[Jmj"),                                                                // matches the launcher version id: 15w45a               15w44b               15w44a
	_15w43c            ("15w43c",             "qsoombkapb[Llq;mn[J[[Jmj"),                                                                // matches the launcher version id: 15w43c               15w43b
	_15w42a            ("15w42a",             "qnojlzjzow[Llp;ml[J[[Jmh"),                                                                // matches the launcher version id: 15w42a
	_15w41b            ("15w41b",             "qmoilyjyov[Llo;mk[J[[Jmg"),                                                                // matches the launcher version id: 15w41b               15w41a
	_15w40b            ("15w40b",             "qhoelujuor[Llk;mg[J[[Jmc"),                                                                // matches the launcher version id: 15w40b               15w40a               15w39c               15w39b               15w39a               15w38b               15w38a               15w37a
	_15w36d            ("15w36d",             "qgodltjuoq[Lll;mf[J[[Jmb"),                                                                // matches the launcher version id: 15w36d               15w36c               15w36b               15w36a
	_15w35e            ("15w35e",             "qeoclsjuop[Llk;me[J[[Jma"),                                                                // matches the launcher version id: 15w35e               15w35d               15w35c               15w35b               15w35a
	_15w34d            ("15w34d",             "qdoblsjuoo[Lll;me[J[[Jma"),                                                                // matches the launcher version id: 15w34d               15w34c
	_15w33c            ("15w33c",             "qanzlrjtom[Llk;md[J[[Jlz"),                                                                // matches the launcher version id: 15w33c
	_15w32c            ("15w32c",             "pmnvlnjt[Llg;lz[J[[Jlv"),                                                                  // matches the launcher version id: 15w32c               15w32b
	_15w31c            ("15w31c",             "oxnvlnjt[Llg;lz[J[[Jlv"),                                                                  // matches the launcher version id: 15w31c               15w31b               15w31a
	_1_8_9             ("1.8.9",              "orntlljs[Lle;lx[J[[Jlt"),                                                                  // matches the launcher version id: 1.8.9                1.8.8                1.8.7                1.8.6                1.8.5                1.8.4
	_1_8_3             ("1.8.3",              "osnulmjt[Llf;ly[J[[Jlu"),                                                                  // matches the launcher version id: 1.8.3                1.8.2                1.8.2-pre7           1.8.2-pre6           1.8.2-pre5
	_1_8_1             ("1.8.1",              "wduyrdnq[Lqu;sp[J[[Jsa"),                                                                  // matches the launcher version id: 1.8.1                1.8.1-pre5           1.8.1-pre4           1.8.1-pre3           1.8.1-pre2           1.8.1-pre1
	_1_8               ("1.8",                "wbuwrcnp[Lqt;sn[J[[Jry"),                                                                  // matches the launcher version id: 1.8                  1.8-pre3             1.8-pre2             1.8-pre1             14w34d               14w34c               14w34b               14w34a
	_14w21b            ("14w21b",             "tjseoylw[Loq;qd[J[[Jpo"),                                                                  // matches the launcher version id: 14w21b               14w21a
	_14w18b            ("14w18b",             "sxrtonlk[Loe;ps[J[[Jpd"),                                                                  // matches the launcher version id: 14w18b               14w18a               14w17a
	_1_7_10            ("1.7.10",             "riqinckb[Lmt;oi[J[[Jns"),                                                                  // matches the launcher version id: 1.7.10               1.7.10-pre4          1.7.10-pre3          1.7.10-pre2          1.7.10-pre1
	_1_7_9             ("1.7.9",              "rhqhnbkb[Lms;oh[J[[Jnr"),                                                                  // matches the launcher version id: 1.7.9                1.7.8                1.7.7                1.7.6
	_14w02c            ("14w02c",             "qrponkki[Lnb;lv[J[[J"),                                                                    // matches the launcher version id: 14w02c               14w02b               14w02a
	_1_7_5             ("1.7.5",              "qfpfnbjy[Lms;lm[J[[J"),                                                                    // matches the launcher version id: 1.7.5
	_1_7_4             ("1.7.4",              "pzozmvjs[Lmm;lg[J[[J"),                                                                    // matches the launcher version id: 1.7.4                1.7.3                13w49a               13w48b
	_1_7_2             ("1.7.2",              "pvovmsjp[Lmj;ld[J[[J"),                                                                    // matches the launcher version id: 1.7.2
	_13w39b            ("13w39b",             "npmp[Lkn;jh[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w39b               13w39a
	_13w38a            ("13w38a",             "ntmt[Lkm;jg[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w38a               13w37b
	_13w37a            ("13w37a",             "nsms[Lkl;jf[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w37a
	_13w36b            ("13w36b",             "nkmk[Lkd;hw[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w36b
	_13w36a            ("13w36a",             "nkmk[Lkd;hx[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 13w36a
	_1_6_4             ("1.6.4",              "mvlv[Ljs;hn[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6.4                1.6.3
	_1_6_2             ("1.6.2",              "mulu[Ljr;hm[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6.2
	_1_6_1             ("1.6.1",              "msls[Ljp;hk[J[J[J[J[J[[J"),                                                                // matches the launcher version id: 1.6.1
	_1_5_2             ("1.5.2",              "[Bbdzbdrbawemabdsbfybdvngngbeuawfbgeawvawvaxrawbbfqausbjgaycawwaraavybkcavwbjubkila"),     // matches the launcher version id: 1.5.2
	_1_5_1             ("1.5.1",              "[Bbeabdsbawemabdtbfzbdwngngbevawfbgfawvawvaxrawbbfrausbjhaycawwaraavybkdavwbjvbkila"),     // matches the launcher version id: 1.5.1
	_1_4_7             ("1.4.7",              "[Baywayoaaszleaypbavaysmdazratabbaatqatqaulaswbanarnbdzauwatraohastbevasrbenbezbdmbdjkh"), // matches the launcher version id: 1.4.7                1.4.6
	_1_4_5             ("1.4.5",              "[Bayoaygaasrleayhbakaykmdazfassbapatjatjaueasobacarfbdoaupatkanzaslbekasjbecbenbdbbcykh"), // matches the launcher version id: 1.4.5                1.4.4
	_1_4_2             ("1.4.2",              "[Baxgawyaarjkpawzayyaxclnaxxarkazcasbasbaswargaytaqabcbathascamuardbcxarbbcpbdabbobbljy"), // matches the launcher version id: 1.4.2                1.4.1
	_1_3_2             ("1.3.2",              "[Batkatcaaofjbatdavbatgjwaubaogavfaovaovapnaocauwamxaxvapyaowajqanzayqanxayjaytaxkaxhik"), // matches the launcher version id: 1.3.2
	_1_3_1             ("1.3.1",              "[Batjatbaaoejaatcavaatfjvauaaofaveaouaouapmaobauvamwaxuapxaovajpanyaypanwayiaysaxjaxgij"), // matches the launcher version id: 1.3.1
	_1_3pre            ("1.3pre",             "acl"),                                                                                     // matches the launcher version id:
	_12w27a            ("12w27a",             "acs"),                                                                                     // matches the launcher version id:
	_12w25a            ("12w25a",             "acg"),                                                                                     // matches the launcher version id:
	_12w24a            ("12w24a",             "aca"),                                                                                     // matches the launcher version id:
	_12w22a            ("12w22a",             "ace"),                                                                                     // matches the launcher version id:
	_12w21b            ("12w21b",             "aby"),                                                                                     // matches the launcher version id:
	_12w21a            ("12w21a",             "abm"),                                                                                     // matches the launcher version id:
	_12w19a            ("12w19a",             "aau"),                                                                                     // matches the launcher version id:
	_1_2_5             ("1.2.5",              "[Bkivmaftxdlvqacqcwfcaawnlnlvpjclrckqdaiyxgplhusdakagi[J[Jalfqabv"),                       // matches the launcher version id: 1.2.5                1.2.4
	_1_2_3             ("1.2.3",              "[Bkfviafowzlvmaclcueyaarninivlizlocipzaisxcphhrrzajugf[J[Jakzpwbt"),                       // matches the launcher version id: 1.2.3                1.2.2                1.2.1
	_12w08a            ("12w08a",             "wj"),                                                                                      // matches the launcher version id:
	_12w07b            ("12w07b",             "wd"),                                                                                      // matches the launcher version id:
	_12w06a            ("12w06a",             "wb"),                                                                                      // matches the launcher version id:
	_12w05a            ("12w05a",             "vy"),                                                                                      // matches the launcher version id:
	_12w04a            ("12w04a",             "vu"),                                                                                      // matches the launcher version id:
	_12w03a            ("12w03a",             "vj"),                                                                                      // matches the launcher version id:
	_1_1               ("1.1",                "[Bjsudadrvqluhaarcqevyzmqmqugiokzcepgagqvsonhhrgahqfy[J[Jaitpdbo"),                        // matches the launcher version id: 1.1
	_12w01a            ("12w01a",             "[Bjqtyadmvllucaancpetyumomoubimkxcdpcaglvnokhfrbahkfw[J[Jainozbn"),                        // matches the launcher version id:
	_1_0               ("1.0",                "[Baesmmaijryafvdinqfdrzhabeabexexwadtnglkqdfagvkiahmhsadk[J[Jtkgkyu"),                     // matches the launcher version id: 1.0
	_b1_9_pre6         ("b1.9-pre6",          "uk"),                                                                                      // matches the launcher version id:
	_b1_9_pre5         ("b1.9-pre5",          "ug"),                                                                                      // matches the launcher version id:
	_b1_9_pre4         ("b1.9-pre4",          "uh"),                                                                                      // matches the launcher version id:
	_b1_9_pre3         ("b1.9-pre3",          "to"),                                                                                      // matches the launcher version id:
	_b1_9_pre2         ("b1.9-pre2",          "sv"),                                                                                      // matches the launcher version id:
	_b1_9_pre1         ("b1.9-pre1",          "[Biorvaaitdiryxqcfebwdlcrxhljqbtnkaddtfmvgjpgaeafd[J[Jafanhbe"),                           // matches the launcher version id:
	_b1_8_1            ("b1.8.1",             "[Bhwqpyrrviqswdbzdqurkhqrgviwbomnabjrxmafvoeacfer[J[Jaddmkbb"),                            // matches the launcher version id: b1.8.1               b1.8
	_b1_7_3            ("b1.7.3",             "[Bobcxpyfdndclsdngrjisjdamkpxczvuuqfhvfkvyovyik[J[Jxivscg"),                               // matches the launcher version id: b1.7.3               b1.7.2               b1.7
	_b1_6_6            ("b1.6.6",             "[Bnxcvpufbmdalodlgpjfsecymgptcxvmukffuxkryfvqih[J[Jwzvkce"),                               // matches the launcher version id: b1.6.6               b1.6.5               b1.6.4               b1.6.3               b1.6.2               b1.6.1               b1.6
	_b1_5_01           ("b1.5_01",            "nfcpozetmcukwdfggiprfcslooycruntlextyjzxeurhv[J[Jvyulbz"),                                 // matches the launcher version id: b1.5_01              b1.5
	_b1_4_01           ("b1.4_01",            "lncdmxebichjmcsfkhooxcfkcmwcerqqvefrkisujsbgw[J[Jtervbo"),                                 // matches the launcher version id: b1.4_01
	_b1_4              ("b1.4",               "lncdmxebichjmcsfkhooxcfkcmwcerpqvefrkisujsagw[J[Jterubo"),                                 // matches the launcher version id: b1.4
	_b1_3_01           ("b1.3_01",            "kybymidthccizcnfbhfoicbjpmhbzqfdxquigtmrhgn[J[Jrbbk"),                                     // matches the launcher version id: b1.3_01
	_b1_3b             ("b1.3b",              "kybymidthccizcnfbhfoicbjpmhbzqgdxqvigtnrign[J[Jrcbk"),                                     // matches the launcher version id: b1.3b
	_b1_2_02           ("b1.2_02",            "kbbvlmdnhbzcjesgsnhbyiwllbwpedrprhqsgqega[J[Jpybj"),                                       // matches the launcher version id: b1.2_02              b1.2_01              b1.2
	_b1_1_02           ("b1.1_02",            "jjboksddfbsccehgemjbrifkrbpobdhonhbqvoyfo[J[Joubc"),                                       // matches the launcher version id: b1.1_02              b1.1_01
	_b1_0_2            ("b1.0.2",             "jibokrddfbscceggdmibriekqbpoadhomhaquoxfn[J[Jotbc"),                                       // matches the launcher version id: b1.0.2               b1.0_01              b1.0
	_a1_2_6            ("a1.2.6",             "ivbmkccyfbqbzeafulsbphukbbnnldcnxgqqgoiff[J[Joeba"),                                       // matches the launcher version id: a1.2.6
	_a1_2_5            ("a1.2.5",             "iubmkbcxfbqbydzftlrbphtkabnnkdbnwgpqfohfe[J[Jodba"),                                       // matches the launcher version id: a1.2.5               a1.2.4_01
	_a1_2_3_04         ("a1.2.3_04",          "iubmkbcxfbqbydzftlqbphtkabnnjdbnvgpqeogfe[J[Jocba"),                                       // matches the launcher version id: a1.2.3_04            a1.2.3_02            a1.2.3_01            a1.2.3
	_a1_2_2b           ("a1.2.2b",            "isbmjycwfbqbydyfrlnbphrjxbnngdansgnqbodfd[J[Jnzba"),                                       // matches the launcher version id: a1.2.2b              a1.2.2a
	_a1_2_1_01         ("a1.2.1_01",          "imbkjrcudbobwdufmlgbnhmjqblmzcynlgiptnv[J[Jnray"),                                         // matches the launcher version id: a1.2.1_01            a1.2.1               a1.2.0_02            a1.2.0_01            a1.2.0
	_a1_1_2_01         ("a1.1.2_01",          "hqbeircnebibqdleykdbhgriqbflucrmffrofmp[Jmlat"),                                           // matches the launcher version id: a1.1.2_01            a1.1.2
	_a1_1_0            ("a1.1.0",             "hqbeircnebibqdleykdbhgriqbflucrmffroemo[Jmlat"),                                           // matches the launcher version id: a1.1.0
	_a1_0_17_04        ("a1.0.17_04",         "hpbdiqcmebhbpdkexkbbggqipbeltcqmdfqobmm[Jmjar"),                                           // matches the launcher version id: a1.0.17_04           a1.0.17_02
	_a1_0_16           ("a1.0.16",            "hgazihcjebebmdferjtbdgiigbblkcnlvfinrmd[Jmbap"),                                           // matches the launcher version id: a1.0.16
	_a1_0_15           ("a1.0.15",            "hfazigcjebebmdferjsbdgiifbbljcnlufinqmc[Jmaap"),                                           // matches the launcher version id: a1.0.15
	_a1_0_14           ("a1.0.14",            "hcazidcjebebmdfeqjpbdghicbblfcnlpfhnmly[Jlwap"),                                           // matches the launcher version id: a1.0.14
	_a1_0_13           ("a1.0.13",            "haazibcjebebmdeeojnbdgfiabblbcnllffnhlu[Jlsap"),                                           // matches a1.0.13	a1.0.13_01-1	a1.0.13_01-2
	_a1_0_12           ("a1.0.12",            "haazibcjebebmdeeojmbdgfiabblacnljffnels[Jlqap"),                                           // matches
	_a1_0_11           ("a1.0.11",            "haaziacjebebmddenjlbdgfhzbbkzcnljfenels[Jlqap"),                                           // matches the launcher version id: a1.0.11
	_a1_0_10           ("a1.0.10",            "guazhucjebebmdceljdbcgbhtbbkrcmlafcmvlj[Jlhap"),                                           // matches
	_a1_0_8_01         ("a1.0.8_01",          "grazhqciebebldaejizbcfyhpbbkmclkvfamqle[Jlcap"),                                           // matches
	_a1_0_8            ("a1.0.8",             "gqazhpciebebldaejiybcfyhobbklclkufampld[Jlbap"),                                           // matches
	_a1_0_7            ("a1.0.7",             "goayhnchebdbkczehiwbbfwhmbakjckkreymmla[Jkyao"),                                           // matches
	_a1_0_6            ("a1.0.6",             "gmaxhlcfebcbjcxefiubafuhkazkfciknewmhkv[Jktan"),                                           // matches a1.0.6		a1.0.6_01		a1.0.6_02
	_a1_0_5_01         ("a1.0.5_01",          "{applet}-2068753250"),                                                                     // matches the launcher version id: a1.0.5_01
	_a1_0_5_2          ("a1.0.5-2",           "{applet}466305486"),                                                                       // matches
	_a1_0_4            ("a1.0.4",             "{applet}790730534"),                                                                       // matches the launcher version id: a1.0.4
	_a1_0_3            ("a1.0.3",             "{applet}-1822015641"),                                                                     // matches
	_a1_0_2_02         ("a1.0.2_02",          "{applet}-538278088"),                                                                      // matches
	_a1_0_2_01         ("a1.0.2_01",          "{applet}-1691321407"),                                                                     // matches
	_a1_0_1_01         ("a1.0.1_01",          "{applet}-385336136"),                                                                      // matches
	_infdev_20100630   ("infdev-20100630",    "{applet}555462259"),                                                                       // matches
	_infdev_20100629   ("infdev-20100629",    "{applet}-409825612"),                                                                      // matches
	_infdev_20100627   ("infdev-20100627",    "{applet}-817349968"),                                                                      // matches
	_infdev_20100625_2 ("infdev-20100625-2",  "{applet}-1106514455"),                                                                     // matches
	_infdev_20100624   ("infdev-20100624",    "{applet}658215649"),                                                                       // matches infdev-20100624	infdev-20100625-1
	_inf_20100618      ("inf-20100618",       "{applet}638268170"),                                                                       // matches the launcher version id: inf-20100618
	_infdev_20100617_2 ("infdev-20100617-2",  "{applet}-1501378303"),                                                                     // matches
	_infdev_20100617_1 ("infdev-20100617-1",  "{applet}-588538399"),                                                                      // matches
	_infdev_20100615   ("infdev-20100615",    "{applet}725646278"),                                                                       // matches
	_infdev_20100611   ("infdev-20100611",    "{applet}-1700143564"),                                                                     // matches
	_infdev_20100607   ("infdev-20100607",    "{applet}1155837323"),                                                                      // matches infdev-20100607	infdev-20100608
	_infdev_20100420   ("infdev-20100420",    "{applet}14002109"),                                                                        // matches
	_infdev_20100415   ("infdev-20100415",    "{applet}-630454512"),                                                                      // matches
	_infdev_20100413   ("infdev-20100413",    "{applet}904522959"),                                                                       // matches infdev-20100413	infdev-20100414
	_infdev_20100330   ("infdev-20100330",    "{applet}1291748709"),                                                                      // matches infdev-20100330
	_infdev_20100327   ("infdev-20100327",    "{applet}1022588581"),                                                                      // matches infdev-20100327
    ;
	// @formatter:on

	@NotNull
	public static RecognisedVersion from(URLClassLoader classLoader) throws ClassNotFoundException {
		return from(generateMagicString(classLoader));
	}

	@NotNull
	public static String generateMagicString(URLClassLoader classLoader) throws ClassNotFoundException {
		return generateMagicString(getMainClassFields(classLoader), classLoader);
	}

	@NotNull
	private static Field[] getMainClassFields(URLClassLoader classLoader) throws ClassNotFoundException {
		try {
			if (classLoader.findResource(CLIENT_CLASS_RESOURCE) != null) {
				return classLoader.loadClass(CLIENT_CLASS).getDeclaredFields();
			} else if (classLoader.findResource(SERVER_CLASS_RESOURCE) != null) {
				return classLoader.loadClass(SERVER_CLASS).getDeclaredFields();
			} else if (classLoader.findResource(APPLET_CLASS_RESOURCE) != null) {
				return new Field[0];
			} else {
				throw new ClassNotFoundException("unable to find the main class in the given jar file");
			}
		} catch (NoClassDefFoundError e) {
			throw new ClassNotFoundException("error while loading main class; are some libraries missing?", e);
		}
	}

	@NotNull
	private static String generateMagicString(Field[] fields, URLClassLoader classLoader) throws ClassNotFoundException {
		StringBuilder result = new StringBuilder();
		for (Field field : fields) {
			String typeString = field.getType().toString();
			if (typeString.startsWith("class ") && !typeString.contains(".")) {
				result.append(typeString.substring(6));
			}
		}
		if (result.length() == 0) {
			// We got an applet version that refuses to comply with version detection.
			// Solution: throw more classes into the mix until the hashes are unique enough.
		    result.append("{applet}");
		    StringBuilder mess = new StringBuilder();
			List<String> classes = Stream.of(
					Stream.of("a", "b", "c", "d", "e", "f").map(c -> "net.minecraft.a.a." + c),
					Stream.of("a", "b", "c", "d").map(c -> "net.minecraft.a.a.e." + c),
					Stream.of("a", "b", "c", "d").map(c -> "net.minecraft.a.c.c." + c),
					Stream.of("ij", "jm", "fz", "ex", "aq", "fi")
			).flatMap(Function.identity()).collect(Collectors.toList());
			for (String name : classes) {
				if (classLoader.findResource(name.replace(".", "/") + ".class") == null)
					continue;
				Class<?> clazz = classLoader.loadClass(name);
				// Turns out that the ordering of elements can change by simply restarting the JVM, so I'll sort them...
				Stream.of(
						Arrays.stream(clazz.getDeclaredFields()).map(Object::toString),
						Arrays.stream(clazz.getDeclaredMethods()).map(Object::toString))
					.flatMap(Function.identity())
					.sorted().forEach(mess::append);
			}
			result.append(mess.toString().hashCode());
		}
		return result.toString();
	}

	@NotNull
	public static RecognisedVersion from(String magicString) {
		for (RecognisedVersion recognisedVersion : RecognisedVersion.values()) {
			if (magicString.equals(recognisedVersion.magicString)) {
				logFound(recognisedVersion);
				return recognisedVersion;
			}
		}
		AmidstLogger.info("Unable to recognise Minecraft Version with the magic string \"" + magicString + "\".");
		return RecognisedVersion.UNKNOWN;
	}

	@NotNull
	public static RecognisedVersion fromName(String name) {
		for (RecognisedVersion recognisedVersion : RecognisedVersion.values()) {
			if (name.equals(recognisedVersion.name)) {
				logFound(recognisedVersion);
				return recognisedVersion;
			}
		}
		AmidstLogger.info("Unable to recognise Minecraft Version with the name \"" + name + "\".");
		return RecognisedVersion.UNKNOWN;
	}

	private static void logFound(RecognisedVersion recognisedVersion) {
		AmidstLogger.info(
				"Recognised Minecraft Version " + recognisedVersion.name + " with the magic string \""
						+ recognisedVersion.magicString + "\".");
	}

	public static boolean isNewerOrEqualTo(RecognisedVersion version1, RecognisedVersion version2) {
		return compareNewerIsLower(version1, version2) <= 0;
	}

	public static boolean isNewer(RecognisedVersion version1, RecognisedVersion version2) {
		return compareNewerIsLower(version1, version2) < 0;
	}

	public static boolean isOlderOrEqualTo(RecognisedVersion version1, RecognisedVersion version2) {
		return compareNewerIsLower(version1, version2) >= 0;
	}

	public static boolean isOlder(RecognisedVersion version1, RecognisedVersion version2) {
		return compareNewerIsLower(version1, version2) > 0;
	}

	public static int compareNewerIsGreater(RecognisedVersion version1, RecognisedVersion version2) {
		return compareNewerIsLower(version2, version1);
	}

	public static int compareNewerIsLower(RecognisedVersion version1, RecognisedVersion version2) {
		Objects.requireNonNull(version1);
		Objects.requireNonNull(version2);
		return version1.ordinal() - version2.ordinal();
	}

	public static Map<String, RecognisedVersion> generateNameToRecognisedVersionMap() {
		Map<String, RecognisedVersion> result = new LinkedHashMap<>();
		for (RecognisedVersion recognisedVersion : RecognisedVersion.values()) {
			if (result.containsKey(recognisedVersion.getName())) {
				RecognisedVersion colliding = result.get(recognisedVersion.getName());
				throw new RuntimeException(
						"name collision for the recognised versions " + recognisedVersion.getName() + " and "
								+ colliding.getName());
			} else {
				result.put(recognisedVersion.getName(), recognisedVersion);
			}
		}
		return result;
	}

	public static Map<String, RecognisedVersion> generateMagicStringToRecognisedVersionMap() {
		Map<String, RecognisedVersion> result = new LinkedHashMap<>();
		for (RecognisedVersion recognisedVersion : RecognisedVersion.values()) {
			if (result.containsKey(recognisedVersion.getMagicString())) {
				RecognisedVersion colliding = result.get(recognisedVersion.getMagicString());
				throw new RuntimeException(
						"magic string collision for the recognised versions " + recognisedVersion.getName() + " and "
								+ colliding.getName());
			} else {
				result.put(recognisedVersion.getMagicString(), recognisedVersion);
			}
		}
		return result;
	}

	public static String createEnumIdentifier(String name) {
		return "_" + name.replaceAll("[^a-zA-Z0-9]", "_");
	}

	private static final String CLIENT_CLASS_RESOURCE = "net/minecraft/client/Minecraft.class";
	private static final String CLIENT_CLASS = "net.minecraft.client.Minecraft";
	private static final String SERVER_CLASS_RESOURCE = "net/minecraft/server/MinecraftServer.class";
	private static final String SERVER_CLASS = "net.minecraft.server.MinecraftServer";
	// The "main" class from back when minecraft was a Java Applet (pre-alpha)
	private static final String APPLET_CLASS_RESOURCE = "net/minecraft/client/MinecraftApplet.class";
	private static final String APPLET_CLASS = "net.minecraft.client.MinecraftApplet";

	private final boolean isKnown;
	private final String name;
	private final String magicString;

	private RecognisedVersion() {
		this.isKnown = false;
		this.name = "UNKNOWN";
		this.magicString = null;
	}

	private RecognisedVersion(String name, String magicString) {
		this.isKnown = true;
		this.name = name;
		this.magicString = magicString;
	}

	public boolean isKnown() {
		return isKnown;
	}

	public String getName() {
		return name;
	}

	public String getMagicString() {
		return magicString;
	}
}
