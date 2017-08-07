package com.google.zxing.client.android.result;

import android.app.Activity;
import android.widget.Toast;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.libs.zxing.R;


public class DefaultResultHandler extends ResultHandler
{

    public DefaultResultHandler(Activity activity, ParsedResult result, Result rawResult)
    {
        super (activity, result, rawResult);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getButtonCount(){
        return 1;
    }

    @Override
    public int getButtonText(int index){
        return R.string.button_2;
    }

    @Override
    public void handleButtonPress(int index){
        Toast.makeText (getActivity(), getResult ().getDisplayResult (), Toast.LENGTH_SHORT).show ();
    }

    @Override
    public int getDisplayTitle(){
        return R.string.button_3;
    }

}
