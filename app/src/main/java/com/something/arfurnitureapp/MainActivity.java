package com.something.arfurnitureapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        arFragment= (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentzz);
//        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
//            Anchor anchor = hitResult.createAnchor();
//            ModelRenderable.builder().
//                    setSource(this, Uri.parse("ArcticFox_Posed.sfa")).
//                    build().
//                    thenAccept(modelRenderable -> addModelToScene(anchor,modelRenderable)).
//                    exceptionally(throwable -> {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                        builder.setMessage(throwable.getMessage()).show();
//                        return null;
//
//                    });
//        }));



        //testing arrrrrrrrrrrrrrrrrrrrrr
//        arFragment= (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentzz);
//        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) -> {
//            Anchor anchor = hitResult.createAnchor();
//            MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.RED)).
//                    thenAccept(material -> {
//                        ModelRenderable renderable = ShapeFactory.makeSphere(1.0f,new Vector3(0f,1f,1f),material);
//                        AnchorNode anchorNode = new AnchorNode(anchor);
//                        anchorNode.setRenderable(renderable);
//                        arFragment.getArSceneView().getScene().addChild(anchorNode);
//                    });
//
//        }));



       // FirebaseApp.initializeApp(this);



//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference modelRef = storage.getReference().child("out.glb");
//
//        ArFragment arFragment = (ArFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.arFragment);
//       findViewById(R.id.downloadBtn).setOnClickListener(new View.OnClickListener() {
//           @Override
//           public void onClick(View view) {
//               try {
//                   File file = File.createTempFile("out","glb");
//                   modelRef.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                       @Override
//                       public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                           buildModel(file);
//                       }
//                   });
//
//               } catch (IOException e) {
//                   e.printStackTrace();
//               }
//           }
//       });
//
//        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
//
//            AnchorNode anchorNode = new AnchorNode(hitResult.createAnchor());
//            anchorNode.setRenderable(renderable);
//            arFragment.getArSceneView().getScene().addChild(anchorNode);
//
//        });

    }
//    private  ModelRenderable renderable;
//
//    void  buildModel(File file){
//        RenderableSource renderableSource = RenderableSource
//                .builder()
//                .setSource(this, Uri.parse(file.getPath()), RenderableSource.SourceType.GLB)
//                .setRecenterMode(RenderableSource.RecenterMode.ROOT)
//                .build();
//
//        ModelRenderable
//                .builder()
//                .setSource(this, renderableSource)
//                .setRegistryId(file.getPath())
//                .build()
//                .thenAccept(modelRenderable -> {
//                    Toast.makeText(this, "Model built", Toast.LENGTH_SHORT).show();;
//                    renderable = modelRenderable;
//                });
//    }

//    private void addModelToScene(Anchor anchor, ModelRenderable modelRenderable) {
//        AnchorNode anchorNode = new AnchorNode(anchor);
//        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
//        transformableNode.setParent(anchorNode);
//        transformableNode.setRenderable(modelRenderable);
//        arFragment.getArSceneView().getScene().addChild(anchorNode);
//        transformableNode.select();
//    }
}
