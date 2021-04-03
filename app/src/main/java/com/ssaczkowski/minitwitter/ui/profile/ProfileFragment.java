package com.ssaczkowski.minitwitter.ui.profile;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.ssaczkowski.minitwitter.R;
import com.ssaczkowski.minitwitter.common.Constant;
import com.ssaczkowski.minitwitter.data.ProfileViewModel;
import com.ssaczkowski.minitwitter.retrofit.request.RequestUserProfile;
import com.ssaczkowski.minitwitter.retrofit.response.ResponseUserProfile;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private ImageView ivAvatar;
    private EditText etUsername, etEmail, etPassword, etWebsite, etDescription;
    private Button btnSave, btnChagePassword;
    private PermissionListener allPermissionsListener;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);


        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        etUsername = view.findViewById(R.id.editTextUserName);
        etEmail = view.findViewById(R.id.editTextEmail);
        etPassword = view.findViewById(R.id.editTextCurrentPassword);
        etWebsite = view.findViewById(R.id.editTextWebSite);
        etDescription = view.findViewById(R.id.editTextDescription);
        btnSave = view.findViewById(R.id.buttonSave);
        btnChagePassword = view.findViewById(R.id.buttonChangePassword);

        //Events
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String descripcion = etDescription.getText().toString();
                String website = etWebsite.getText().toString();
                String password = etPassword.getText().toString();

                if(checkDataRequired(username, email, password)) {
                    RequestUserProfile requestUserProfile = new RequestUserProfile(username, email, descripcion, website, password);
                    mViewModel.updateProfile(requestUserProfile);
                    Toast.makeText(getActivity(), R.string.sending_information,Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(false);
                }
            }
        });

        btnChagePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Click on change password", Toast.LENGTH_SHORT).show();
            }
        });

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkPermissions();
            }
        });

        //ViewModel
        mViewModel.userProfile.observe(getActivity(), new Observer<ResponseUserProfile>() {
            @Override
            public void onChanged(ResponseUserProfile responseUserProfile) {
                etUsername.setText(responseUserProfile.getUsername());
                etDescription.setText(responseUserProfile.getDescripcion());
                etEmail.setText(responseUserProfile.getEmail());
                etWebsite.setText(responseUserProfile.getWebsite());

                btnSave.setEnabled(true);
                Toast.makeText(getActivity(), R.string.saved_successfully,Toast.LENGTH_SHORT).show();

                if(!responseUserProfile.getPhotoUrl().isEmpty()){
                    Glide.with(getActivity()).load(Constant.API_MINITWITTER_FILES_URL + responseUserProfile.getPhotoUrl())
                            .dontAnimate()
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(ivAvatar);
                }
            }
        });

        return view;
    }

    private void checkPermissions() {
        PermissionListener dialogOnDeniedPermissionListener = DialogOnDeniedPermissionListener.Builder.withContext(getActivity())
                .withTitle(R.string.permissions)
                .withMessage(R.string.profile_photo_permissions_dialog)
                .withButtonText(R.string.agree)
                .withIcon(R.mipmap.ic_launcher)
                .build();

        allPermissionsListener = new CompositePermissionListener((PermissionListener) getActivity(),
                dialogOnDeniedPermissionListener);

        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(allPermissionsListener)
                .check();
    }

    private boolean checkDataRequired(String username, String email, String password) {
        boolean isValid = false;

        if(username.isEmpty()){
            etUsername.setError(getString(R.string.username_required));
        } else if(email.isEmpty()){
            etEmail.setError(getString(R.string.email_required));
        } else if(password.isEmpty()){
            etPassword.setError(getString(R.string.password_required));
        } else {
            isValid = true;
        }
        return isValid;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

    }

}