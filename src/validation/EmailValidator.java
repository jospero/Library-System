//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package validation;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@DefaultProperty("icon")
public class EmailValidator extends ValidatorBase {
    public EmailValidator() {
    }

    protected void eval() {
        if(this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }

    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();

        Pattern VALID_Email_ADDRESS_REGEX =
                Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_Email_ADDRESS_REGEX.matcher(textField.getText());
        this.hasErrors.set(!matcher.find());

//        try {
//            Integer.parseInt(textField.getText());
//            this.hasErrors.set(false);
//        } catch (Exception var3) {
//            this.hasErrors.set(true);
//        }

    }
}
