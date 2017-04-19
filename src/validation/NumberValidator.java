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
public class NumberValidator extends ValidatorBase {
    public NumberValidator() {
    }

    protected void eval() {
        if(this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }

    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();

        Pattern VALID_NUMBER_REGEX =
                Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = VALID_NUMBER_REGEX.matcher(textField.getText());

        this.hasErrors.set(!matcher.find());

    }
}
