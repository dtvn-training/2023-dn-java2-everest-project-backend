package com.dtvn.springbootproject.utils;

import lombok.Getter;

@Getter
public class RegularExpression {
    public static final String PHONE_NUMBER_REGEX = "^\\(?(\\+\\d{1,3})?\\)?[-.\\s]?\\d{1,3}[-.\\s]?\\d{3,5}[-.\\s]?\\d{4}(?:\\s?(\\w{1,10})\\s?(\\d{1,6}))?$";
    public static final String NAME_REGEX = "^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$|^[一-龠ぁ-ゔァ-ヴーa-zA-Z0-9ａ-ｚＡ-Ｚ０-９々〆〤ヶ]+$";
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    public static final String ADDRESS_REGEX = "^[a-zA-Z0-9\\s,'-.]+";
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,50}$";
}

