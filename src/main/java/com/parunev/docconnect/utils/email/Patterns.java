package com.parunev.docconnect.utils.email;

import com.parunev.docconnect.models.Appointment;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

/**
 * A utility class for generating email patterns and templates.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Patterns {

    /**
     * Build a confirmation email pattern with the provided name and link.
     *
     * @param name The recipient's name.
     * @param link The verification link to be included in the email.
     * @return A string containing the confirmation email template.
     */
    public static String buildConfirmationEmail(String name, String link) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c\">\n"
                + "\n"
                + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100% !important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n"
                + "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
                + "            <tbody>\n"
                + "              <tr>\n"
                + "                <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
                + "                  <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n"
                + "                    <tbody>\n"
                + "                      <tr>\n"
                + "                        <td style=\"padding-left: 10px\"></td>\n"
                + "                        <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px\">\n"
                + "                          <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block\">Confirm your email</span>\n"
                + "                        </td>\n"
                + "                      </tr>\n"
                + "                    </tbody>\n"
                + "                  </table>\n"
                + "                </td>\n"
                + "              </tr>\n"
                + "            </tbody>\n"
                + "          </table>\n"
                + "        </td>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + " <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important\" width=\"100%\">\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                + "        <td>\n"
                + "          <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n"
                + "            <tbody>\n"
                + "              <tr>\n"
                + "                <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
                + "              </tr>\n"
                + "            </tbody>\n"
                + "          </table>\n"
                + "        </td>\n"
                + "        <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + "\n"
                + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important\" width=\"100%\">\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <td height=\"30\"><br></td>\n"
                + "      </tr>\n"
                + "      <tr>\n"
                + "        <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "        <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px\">\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Dear <b>"
                + name
                + "</b>,</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Thank you for choosing <b>DocConnect</b>, your trusted medical appointment application. We are excited to have you on board and be a part of our growing community of healthcare enthusiasts.</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">To ensure the security of your account and start accessing our advanced features, we kindly ask you to verify your email address. Verification is a quick and simple process that takes only a moment of your time but greatly enhances the safety and reliability of your <b>DocConnect</b> account.</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Please click on the link below to verify your email:</p>\n"
                + "          <blockquote style=\"Margin: 0 0 20px 0; border-left: 10px solid #b1b4b6; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px\">\n"
                + "            <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">\n"
                + "              <a href=\""
                + link
                + "\">Activate Now</a>\n"
                + "            </p>\n"
                + "          </blockquote>\n"
                + "        <p style=\"color: #000;\">The link will expire in <b>24</b> hours.</p>\n"
                + "        <p style=\"color: #000;\">Best regards,<br>\n"
                + "                                  <b>The DocConnect Team</b></p>\n"
                + "        </td>\n"
                + "        <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "      </tr>\n"
                + "      <tr>\n"
                + "        <td height=\"30\"><br></td>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + "  <div class=\"yj6qo\"></div>\n"
                + "  <div class=\"adL\">\n"
                + "  </div>\n"
                + "</div>";
    }

    public static String buildRegistrationSuccessEmail(String name) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c\">\n"
                + "\n"
                + "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100% !important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n"
                + "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n"
                + "            <tbody>\n"
                + "              <tr>\n"
                + "                <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n"
                + "                  <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n"
                + "                    <tbody>\n"
                + "                      <tr>\n"
                + "                        <td style=\"padding-left: 10px\"></td>\n"
                + "                        <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px\">\n"
                + "                          <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block\">Your DocConnect Account has been confirmed!</span>\n"
                + "                        </td>\n"
                + "                      </tr>\n"
                + "                    </tbody>\n"
                + "                  </table>\n"
                + "                </td>\n"
                + "              </tr>\n"
                + "            </tbody>\n"
                + "          </table>\n"
                + "        </td>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + " <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important\" width=\"100%\">\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n"
                + "        <td>\n"
                + "          <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse\">\n"
                + "            <tbody>\n"
                + "              <tr>\n"
                + "                <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n"
                + "              </tr>\n"
                + "            </tbody>\n"
                + "          </table>\n"
                + "        </td>\n"
                + "        <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + "\n"
                + "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important\" width=\"100%\">\n"
                + "    <tbody>\n"
                + "      <tr>\n"
                + "        <td height=\"30\"><br></td>\n"
                + "      </tr>\n"
                + "      <tr>\n"
                + "        <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "        <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px\">\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Dear <b>"
                + name
                + "</b>,</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">We are thrilled to inform you that your <b>DocConnect</b> account has been successfully confirmed!</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">Welcome to the <b>DocConnect community</b>, where you can connect with patients, colleagues, and stay updated on the latest developments in the medical field.</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">With your DocConnect account, you can:</p>\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\">\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\"><b>Connect with Patients</b>: Build a strong patient-doctor relationship by connecting with your patients online. Share important health information, answer questions, and provide guidance, all within a secure and convenient platform.</p>\n\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\"><b>Collaborate with Colleagues</b>: Network with fellow healthcare professionals, discuss complex cases, and exchange knowledge to enhance patient care.</p>\n\n"
                + "          <p style=\"Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c\"><b>Stay Informed</b>: Access the latest medical research, news, and updates, ensuring that you remain at the forefront of your field.</p>\n\n"
                + "        <p style=\"color: #000;\">Thank you for choosing DocConnect as your trusted platform for medical communication and collaboration. We look forward to supporting you in your practice and professional journey.</p>\n\n"
                + "        <p style=\"color: #000;\">Best regards,<br>\n"
                + "                                  <b>The DocConnect Team</b></p>\n"
                + "        </td>\n"
                + "        <td width=\"10\" valign=\"middle\"><br></td>\n"
                + "      </tr>\n"
                + "      <tr>\n"
                + "        <td height=\"30\"><br></td>\n"
                + "      </tr>\n"
                + "    </tbody>\n"
                + "  </table>\n"
                + "  <div class=\"yj6qo\"></div>\n"
                + "  <div class=\"adL\">\n"
                + "  </div>\n"
                + "</div>";
    }


    /**
     * Builds an HTML email template for notifying users about a password change.
     *
     * @param name The name of the recipient.
     * @return A formatted HTML email template as a String.
     */
    public static String buildPasswordChangedEmail(String name) {
        return """
    <div style="font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c">
    
        <table role="presentation" width="100%" style="border-collapse: collapse; min-width: 100%; width: 100% !important" cellpadding="0" cellspacing="0" border="0">
            <tbody>
                <tr>
                    <td width="100%" height="53" bgcolor="#0b0c0c">
                        <table role="presentation" width="100%" style="border-collapse: collapse; max-width: 580px" cellpadding="0" cellspacing="0" border="0" align="center">
                            <tbody>
                                <tr>
                                    <td width="70" bgcolor="#0b0c0c" valign="middle">
                                        <table role="presentation" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                                            <tbody>
                                                <tr>
                                                    <td style="padding-left: 10px"></td>
                                                    <td style="font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px">
                                                        <span style="font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block">Password Changed Notification</span>
                                                    </td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                </tr>
            </tbody>
        </table>
        <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
            <tbody>
                <tr>
                    <td width="10" height="10" valign="middle"></td>
                    <td>
                        <table role="presentation" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                            <tbody>
                                <tr>
                                    <td bgcolor="#1D70B8" width="100%" height="10"></td>
                                </tr>
                            </tbody>
                        </table>
                    </td>
                    <td width="10" valign="middle" height="10"></td>
                </tr>
            </tbody>
        </table>
        <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
            <tbody>
                <tr>
                    <td height="30"><br></td>
                </tr>
                <tr>
                    <td width="10" valign="middle"><br></td>
                    <td style="font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px">
                        <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">Dear <b>"""
                + name
                + """
                        </b>,</p>
                        <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">Your password has been successfully changed. If you did not initiate this change, please contact us immediately.</p>
                        <p style="color: #000;">Best regards,<br>
                            <b>The DocConnect Team</b></p>
                    </td>
                    <td width="10" valign="middle"><br></td>
                </tr>
                <tr>
                    <td height="30"><br></td>
                </tr>
            </tbody>
        </table>
        <div class="yj6qo"></div>
        <div class="adL">
        </div>
    </div>
    
    """;
    }

    public static String buildAppointmentCanceledEmail(
            String patientName,
            String specialistName,
            String specialty,
            String address,
            Appointment appointment) {

        return """
<div style="font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c">

    <table role="presentation" width="100%" style="border-collapse: collapse; min-width: 100%; width: 100% !important" cellpadding="0" cellspacing="0" border="0">
        <tbody>
            <tr>
                <td width="100%" height="53" bgcolor="#0b0c0c">
                    <table role="presentation" width="100%" style="border-collapse: collapse; max-width: 580px" cellpadding="0" cellspacing="0" border="0" align="center">
                        <tbody>
                            <tr>
                                <td width="70" bgcolor="#0b0c0c" valign="middle">
                                    <table role="presentation" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                                        <tbody>
                                            <tr>
                                                <td style="padding-left: 10px"></td>
                                                <td style="font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px">
                                                    <span style="font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block">Appointment Canceled Successfully</span>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>
    <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
        <tbody>
            <tr>
                <td width="10" height="10" valign="middle"></td>
                <td>
                    <table role="presentation" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                        <tbody>
                            <tr>
                                <td bgcolor="#1D70B8" width="100%" height="10"></td>
                            </tr>
                        </tbody>
                    </table>
                </td>
                <td width="10" valign="middle" height="10"></td>
            </tr>
        </tbody>
    </table>
    <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
        <tbody>
            <tr>
                <td height="30"><br></td>
            </tr>
            <tr>
                <td width="10" valign="middle"><br></td>
                <td style="font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px">
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">Dear <b>
                    """
                + patientName
                + """
                    </b>,</p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">You have successfully canceled your appointment for:</p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Doctor:</b>
                    """
                + specialistName
                + """
                    </p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Specialty:</b>
                     """
                + specialty
                + """
                    </p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Location:</b>
                    """
                + address
                + """
                    </p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Date & Time:</b>
                    """
                + appointment.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                + " " + " "
                + appointment.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("ha")).toUpperCase()
                + """
                    </p>

        
                    <p style="color: #000;"><br>Best regards,<br><b>The DocConnect Team</b></p>
                </td>
                <td width="10" valign="middle"><br></td>
            </tr>
            <tr>
                <td height="30"><br></td>
            </tr>
        </tbody>
    </table>
    <div class="yj6qo"></div>
    <div class="adL"></div>
</div>
""";
    }

    public static String buildAppointmentReminderEmail(
            String patientName,
            String specialistName,
            String specialty,
            String address,
            Appointment appointment) {

        return """
<div style="font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c">

    <table role="presentation" width="100%" style="border-collapse: collapse; min-width: 100%; width: 100% !important" cellpadding="0" cellspacing="0" border="0">
        <tbody>
            <tr>
                <td width="100%" height="53" bgcolor="#0b0c0c">
                    <table role="presentation" width="100%" style="border-collapse: collapse; max-width: 580px" cellpadding="0" cellspacing="0" border="0" align="center">
                        <tbody>
                            <tr>
                                <td width="70" bgcolor="#0b0c0c" valign="middle">
                                    <table role="presentation" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                                        <tbody>
                                            <tr>
                                                <td style="padding-left: 10px"></td>
                                                <td style="font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px">
                                                    <span style="font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block">Upcoming Appointment Reminder</span>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </tbody>
    </table>
    <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
        <tbody>
            <tr>
                <td width="10" height="10" valign="middle"></td>
                <td>
                    <table role="presentation" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                        <tbody>
                            <tr>
                                <td bgcolor="#1D70B8" width="100%" height="10"></td>
                            </tr>
                        </tbody>
                    </table>
                </td>
                <td width="10" valign="middle" height="10"></td>
            </tr>
        </tbody>
    </table>
    <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
        <tbody>
            <tr>
                <td height="30"><br></td>
            </tr>
            <tr>
                <td width="10" valign="middle"><br></td>
                <td style="font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px">
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">Dear <b>
                    """
                + patientName
                + """
                    </b>,</p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">We would like to kindly remind you of your upcoming appointment:</p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b><br>Doctor:</b>
                    """
                + specialistName
                + """
                    </p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Specialty:</b>
                     """
                + specialty
                + """
                    </p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Location:</b>
                    """
                + address
                + """
                    </p>
                    <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Date & Time:</b>
                    """
                + appointment.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                + " " + " "
                + appointment.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("ha")).toUpperCase()
                + """
                    </p>

        
                    <p style="color: #000;"><br>Best regards,<br><b>The DocConnect Team</b></p>
                </td>
                <td width="10" valign="middle"><br></td>
            </tr>
            <tr>
                <td height="30"><br></td>
            </tr>
        </tbody>
    </table>
    <div class="yj6qo"></div>
    <div class="adL"></div>
</div>
""";
    }

    public static String buildFeedbackEmail(
            String patientName,
            String specialistName,
            String specialty,
            String address,
            Appointment appointment,
            Long specialistId) {
        return """
  <div style="font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #0b0c0c">

        <table role="presentation" width="100%" style="border-collapse: collapse; min-width: 100%; width: 100% !important" cellpadding="0" cellspacing="0" border="0">
          <tbody>
            <tr>
              <td width="100%" height="53" bgcolor="#0b0c0c">
                <table role="presentation" width="100%" style="border-collapse: collapse; max-width: 580px" cellpadding="0" cellspacing="0" border="0" align="center">
                  <tbody>
                    <tr>
                      <td width="70" bgcolor="#0b0c0c" valign="middle">
                        <table role="presentation" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                          <tbody>
                            <tr>
                              <td style="padding-left: 10px"></td>
                              <td style="font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px">
                                <span style="font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block">Your Feedback is Important</span>
                              </td>
                            </tr>
                          </tbody>
                        </table>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </td>
            </tr>
          </tbody>
        </table>
        <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
          <tbody>
            <tr>
              <td width="10" height="10" valign="middle"></td>
              <td>
                <table role="presentation" width="100%" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse">
                  <tbody>
                    <tr>
                      <td bgcolor="#1D70B8" width="100%" height="10"></td>
                    </tr>
                  </tbody>
                </table>
              </td>
              <td width="10" valign="middle" height="10"></td>
            </tr>
          </tbody>
        </table>
        <table role="presentation" class="m_-6186904992287805515content" align="center" cellpadding="0" cellspacing="0" border="0" style="border-collapse: collapse; max-width: 580px; width: 100% !important" width="100%">
          <tbody>
            <tr>
              <td height="30"><br></td>
            </tr>
            <tr>
              <td width="10" valign="middle"><br></td>
              <td style="font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px">
                <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">Dear <b>
                """
                + patientName
                + """
                  </b>,</p>
                <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">Thank you for choosing DocConnect. We appreciate you choosing DocConnect again as a secure and convenient tool for managing your appointments. </p>

                <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">We value your opinion and feedback. Your experience helps us improve and serve you better. We invite you to visit our website using the link below and leave a review for your last appointment:</p>
                <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Doctor:</b>
               """
                + specialistName
                + """
              </p>
              <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Specialty:</b>
               """
                + specialty
                + """
              </p>
              <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Location:</b>
               """
                + address
                + """
              </p>
              <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c"><b>Date & Time:</b>
             """
                + appointment.getDateTime().toLocalDate().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                + " " + " "
                + appointment.getDateTime().toLocalTime().format(DateTimeFormatter.ofPattern("ha")).toUpperCase()
                + """
                </p>

                <blockquote style="Margin: 0 0 20px 0; border-left: 10px solid #b1b4b6; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px">
                  <p style="Margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #0b0c0c">
                    <a href=
                  """
                + "{YOUR_FRONTEND_URL}"
                + "/specialists/doctor/"
                + specialistId
                + "#create"
                + """
                          >Leave a Review</a>
                  </p>
                </blockquote>

                <p style="color: #000;">Your feedback helps us grow. Thank you for your time!</p>
                <p style="color: #000;">Best regards,<br>
                  <b>The DocConnect Team</b></p>
              </td>
              <td width="10" valign="middle"><br></td>
            </tr>
            <tr>
              <td height="30"><br></td>
            </tr>
          </tbody>
        </table>
        <div class="yj6qo"></div>
        <div class="adL">
        </div>
      </div>
""";
    }


}
