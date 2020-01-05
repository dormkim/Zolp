package com.example.zolup;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialog {

    private Context ccontext;

    public CustomDialog(Context context) {
        this.ccontext = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(final String place_title, final String place_context, int position, boolean check_share) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(ccontext);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final EditText title = dlg.findViewById(R.id.title);
        final EditText context = dlg.findViewById(R.id.context);
        final CheckBox checkBox = dlg.findViewById(R.id.checkButton);
        final Button okButton = dlg.findViewById(R.id.okButton);
        final Button cancelButton = dlg.findViewById(R.id.cancelButton);

        title.setText(place_title);
        context.setText(place_context);
        checkBox.setChecked(check_share);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // '확인' 버튼 클릭시 메인 액티비티에서 설정한 main_label에
                // 커스텀 다이얼로그에서 입력한 메시지를 대입한다.
                boolean check_share = false;
                if(checkBox.isChecked()){
                    check_share = true;
                }
                PrivateMapActivity.updateList(title.getText().toString(), context.getText().toString(), position, check_share);
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ccontext, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                // 커스텀 다이얼로그를 종료한다.
                dlg.dismiss();
            }
        });
    }
}