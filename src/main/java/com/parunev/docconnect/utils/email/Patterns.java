package com.parunev.docconnect.utils.email;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

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

}
