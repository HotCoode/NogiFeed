package shts.jp.android.nogifeed.views.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import shts.jp.android.nogifeed.R;

public class DownloadConfirmDialog extends DialogFragment {

    private static final String TAG = DownloadConfirmDialog.class.getSimpleName();

    /**
     * Callbacks for dialog button click
     */
    public interface Callbakcs {
        public void onClickPositiveButton();
        public void onClickNegativeButton();
    }

    private Callbakcs mCallbakcs;

    public void setCallbacks(Callbakcs callbacks) {
        mCallbakcs = callbacks;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_confirm_download_title);
        builder.setMessage(R.string.dialog_confirm_download_message);
        builder.setPositiveButton(R.string.dialog_confirm_download_positive_btn,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbakcs != null) {
                    mCallbakcs.onClickPositiveButton();
                }
            }
        });
        builder.setNegativeButton(R.string.dialog_confirm_download_negative_btn,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mCallbakcs != null) {
                    mCallbakcs.onClickNegativeButton();
                }
            }
        });
        return builder.create();
    }
}
