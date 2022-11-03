package com.example.tugasmodul7.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tugasmodul7.R;
import com.example.tugasmodul7.database.Note;
import com.example.tugasmodul7.databinding.ActivityInsertUpdateBinding;
import com.example.tugasmodul7.helper.DateHelper;
import com.example.tugasmodul7.helper.ViewModelFactory;

public class InsertUpdateActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "extra_note";
    private final int ALERT_DIALOG_CLOSE = 10;
    private final int ALERT_DIALOG_DELETE = 20;
    private boolean isEdit = false;
    private Note note;
    private NoteInsertUpdateViewModel noteInsertUpdateViewModel;
    private ActivityInsertUpdateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =
                ActivityInsertUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        noteInsertUpdateViewModel =
                obtainViewModel(InsertUpdateActivity.this);
        note = getIntent().getParcelableExtra(EXTRA_NOTE);
        if (note != null) {
            isEdit = true;
        } else {
            note = new Note();
        }
        String actionBarTitle;
        String btnTitle;
        if (isEdit) {
            actionBarTitle = getString(R.string.change);
            btnTitle = getString(R.string.update);
            if (note != null) {
                binding.edtTitle.setText(note.getTitle());

                binding.edtDescription.setText(note.getDescription());
            }
        } else {
            actionBarTitle = getString(R.string.add);
            btnTitle = getString(R.string.save);
        }
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(actionBarTitle);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        binding.btnSubmit.setText(btnTitle);
        binding.btnSubmit.setOnClickListener(view -> {
            String title =
                    binding.edtTitle.getText().toString().trim();
            String description =
                    binding.edtDescription.getText().toString().trim();
            if (title.isEmpty()) {

                binding.edtTitle.setError(getString(R.string.empty));
            } else if (description.isEmpty()) {

                binding.edtDescription.setError(getString(R.string.empty));
            } else {
                note.setTitle(title);
                note.setDescription(description);
                Intent intent = new Intent();
                intent.putExtra(EXTRA_NOTE, note);
                if (isEdit) {
                    noteInsertUpdateViewModel.update(note);
                    showToast(getString(R.string.changed));
                } else {
                    note.setDate(DateHelper.getCurrentDate());
                    noteInsertUpdateViewModel.insert(note);
                    showToast(getString(R.string.added));
                }
                finish();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_form, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            showAlertDialog(ALERT_DIALOG_DELETE);
        } else if (item.getItemId() == android.R.id.home) {
            showAlertDialog(ALERT_DIALOG_CLOSE);
        } else {
            return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE);
    }
    private void showAlertDialog(int type) {
        final boolean isDialogClose = type == ALERT_DIALOG_CLOSE;
        String dialogTitle, dialogMessage;
        if (isDialogClose) {
            dialogTitle = getString(R.string.cancel);
            dialogMessage = getString(R.string.message_cancel);
        } else {
            dialogMessage = getString(R.string.message_delete);
            dialogTitle = getString(R.string.delete);
        }
        AlertDialog.Builder alertDialogBuilder = new
                AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        (dialog, id) -> {
                            if (!isDialogClose) {
                                noteInsertUpdateViewModel.delete(note);
                                showToast(getString(R.string.deleted));
                            }
                            finish();
                        })
                .setNegativeButton(getString(R.string.no),
                        (dialog, id) -> dialog.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void showToast(String message) {
        Toast.makeText(this, message,
                Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
    @NonNull
    private static NoteInsertUpdateViewModel
    obtainViewModel(AppCompatActivity activity) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication());
        return new ViewModelProvider(activity, (ViewModelProvider.Factory) factory).get(NoteInsertUpdateViewModel.class);
    }
}
