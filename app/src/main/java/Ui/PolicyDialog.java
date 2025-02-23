package Ui;
import com.example.androidproject_tamara_hen.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class PolicyDialog extends DialogFragment {

    private PolicyDialogListener listener;

    public interface PolicyDialogListener {
        void onPolicyAccepted();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PolicyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement PolicyDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Purchase Policy");

        // Create layout for the dialog
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Policy Text
        TextView policyText = new TextView(getActivity());
        policyText.setText("By proceeding with this purchase, you agree to our Terms and Conditions. Ensure all details are correct before confirming.");
        policyText.setPadding(10, 10, 10, 10);
        layout.addView(policyText);

        // Confirmation Checkbox
        CheckBox checkBox = new CheckBox(getActivity());
        checkBox.setText("I agree to the terms and conditions");
        layout.addView(checkBox);

        builder.setView(layout);

        // Confirm Button
        builder.setPositiveButton("Confirm", (dialog, which) -> {
            if (checkBox.isChecked()) {
                listener.onPolicyAccepted(); // Notify listener that user accepted
            } else {
                Toast.makeText(getActivity(), "You must agree to the terms to proceed", Toast.LENGTH_SHORT).show();
            }
        });

        // Cancel Button
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
