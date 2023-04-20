package com.asra.mobileapp.ui.general.enrolledevents.passport.uploadpassport;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import com.asra.mobileapp.R;
import com.asra.mobileapp.common.dialog.ETConfirmationDialog;
import com.asra.mobileapp.common.dialog.ETDialog;
import com.asra.mobileapp.common.imageloader.ImageUtils;
import com.asra.mobileapp.databinding.FragmentPassportSignatureBinding;
import com.asra.mobileapp.ui.base.ETFragment;

import java.io.File;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.core.text.HtmlCompat;
import timber.log.Timber;

public class PassportSignatureFragment extends ETFragment<PassportSignatureViewModel, FragmentPassportSignatureBinding> {


    public static PassportSignatureFragment newInstance(){
        PassportSignatureFragment fragment = new PassportSignatureFragment();
        return fragment;
    }
    private Uri mImageUri;

    ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    try {

                       // Bundle extras = result.getData().getExtras();
                        //Bitmap imageBitmap = (Bitmap) extras.get("data");
                        Bitmap imageBitmap = getCapturedImage();
                        String profileImagePath = ImageUtils.prepareImageFromBitmap(imageBitmap);
                        viewModel.onImageCaptured(profileImagePath);
                    }catch (Exception e){
                        Timber.e(e);
                    }
                }
            });

    @Override
    protected Class<PassportSignatureViewModel> getViewModel() {
        return PassportSignatureViewModel.class;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_passport_signature;
    }

    @Override
    public void initializeViews() {

        getBaseActivity().requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE});
        //getBaseActivity().requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, "Evolve GT needs media permission to upload the selfie.");

        ImageUtils.cameraCheck(getContext());
        dataBinding.btnUploadSelfie.setOnClickListener(view -> {
            pickImage();
        });
        dataBinding.btnSaveSign.setOnClickListener(view -> {
            if(!dataBinding.signaturePad.isEmpty()) {
                viewModel.savePassport(dataBinding.signaturePad.getSignatureBitmap());
            }
        });

        dataBinding.cbAgreeTerms.setOnCheckedChangeListener((compoundButton, checked) -> {
            dataBinding.btnSaveSign.setEnabled(checked);
        });
        dataBinding.btnCloseSignPad.setOnClickListener(view -> {
            ETConfirmationDialog confirmationDialog = new ETConfirmationDialog(getActivity(), new ETConfirmationDialog.ConfirmationListener() {
                @Override
                public void onConfirmed(Object passthrough) {
                    popFragment();
                }
            },null);
            confirmationDialog.setDialogBtnPositive("Exit");
            confirmationDialog.setDialogMessage("Your changes will be discarded.\nWould you like to exit?");
            confirmationDialog.show();
        });

        dataBinding.btnClearSignPad.setOnClickListener(view -> {
            dataBinding.signaturePad.clear();
        });
    }

    @Override
    public void observeEventsFromViewModel() {
        viewModel.passportSavedObservable.observe(this, saved ->{
            showPassportSavedDialog();
        });
        viewModel.passportSaveFailureObservable.observe(this, this::showErrorMessage);
        viewModel.disclaimerObservable.observe(this, disclaimer ->
                dataBinding.tncContent.setText(HtmlCompat.fromHtml(disclaimer,
                        HtmlCompat.FROM_HTML_MODE_LEGACY)));
    }
    private void showPassportSavedDialog() {
        ETDialog dialog = new ETDialog(getActivity(), new ETDialog.DialogListener() {
            @Override
            public void onPositiveButtonClicked() {
                super.onPositiveButtonClicked();
                popFragment();
            }
        });
        dialog.setDialogTitle("Signature Saved");
        dialog.setDialogMessage("Passport saved successfully.");
        dialog.show();

    }
    @Override
    public String getTitle() {
        return getString(R.string.title_i_am_here);
    }

    private void openCamera(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo;
        try
        {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("selfie", ".jpg");
            photo.delete();
            //mImageUri = Uri.fromFile(photo);
            mImageUri = FileProvider.getUriForFile(getActivity(),
                    getActivity().getApplicationContext().getPackageName() + ".provider",
                    photo);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraLauncher.launch(intent);
        }
        catch(Exception e) {
            Timber.e(e, "Can't create file to take picture!");
            Toast.makeText(getActivity(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_LONG).show();
        }

    }
    private File createTemporaryFile(String part, String ext) throws Exception
    {
        File tempDir= getContext().getFilesDir();
        tempDir = new File(tempDir.getAbsolutePath());
        if(!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(part, ext, tempDir);
    }
    private void pickImage(){

        openCamera();
//        TedImagePicker.with(getContext())
//                .start(uri -> {
//                    Timber.i("Selected Image %s", uri.getPath());
//                    String profileImagePath = ImageUtils.prepareImageFromUri(uri);
//                    viewModel.onImageCaptured(profileImagePath);
//                });
    }

    @Override
    public void updateToEvAppTheme() {
        super.updateToEvAppTheme();
        dataBinding.btnUploadSelfie.setBackgroundResource(R.drawable.round_rect_green);
        dataBinding.btnSaveSign.setBackgroundResource(R.drawable.selector_button_primary);
    }

    @Override
    public void updateToMotoAppTheme() {
        super.updateToMotoAppTheme();
        dataBinding.btnUploadSelfie.setBackgroundResource(R.drawable.round_rect_moto);
        dataBinding.btnSaveSign.setBackgroundResource(R.drawable.selector_button_moto_primary);


    }

    public Bitmap getCapturedImage()
    {
        getActivity().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = getActivity().getContentResolver();
        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(cr, mImageUri);
        }
        catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to load", Toast.LENGTH_SHORT).show();
            Timber.e(e, "Failed to load");
        }
        return bitmap;
    }
}
