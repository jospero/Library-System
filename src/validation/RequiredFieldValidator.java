package validation;//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextInputControl;

@DefaultProperty("icon")
public class RequiredFieldValidator extends ValidatorBase {
    public RequiredFieldValidator() {
    }

    protected void eval() {
        if(this.srcControl.get() instanceof TextInputControl) {
            this.evalTextInputField();
        }

        if(this.srcControl.get() instanceof JFXComboBox) {
            this.evalComboBoxField();
        }

    }

    private void evalTextInputField() {
        TextInputControl textField = (TextInputControl)this.srcControl.get();
        if(textField.getText() != null && !textField.getText().trim().isEmpty()) {
            this.hasErrors.set(false);
        } else {
            this.hasErrors.set(true);
        }

    }

    private void evalComboBoxField() {
        JFXComboBox<?> comboField = (JFXComboBox)this.srcControl.get();
        boolean hasValue = comboField.getValue() != null;
        boolean editorHasNonEmptyText = comboField.isEditable() && comboField.getEditor().getText() != null && !comboField.getEditor().getText().trim().isEmpty();
        this.hasErrors.set(!hasValue && !editorHasNonEmptyText);
    }
}
