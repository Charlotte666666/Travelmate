package com.jayrun.travelmate;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ChangePwdActivity extends Activity implements OnClickListener {
	private Button back;
	private Button submit;
	private EditText oldPwd;
	private EditText newPwd1;
	private EditText newPwd2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_pwd);
		init();
	}

	private void init() {
		back = (Button) findViewById(R.id.change_pwd_back);
		back.setOnClickListener(this);
		submit = (Button) findViewById(R.id.change_password_ok);
		submit.setOnClickListener(this);
		oldPwd = (EditText) findViewById(R.id.change_pwd_old);
		newPwd1 = (EditText) findViewById(R.id.change_pwd_new1);
		newPwd2 = (EditText) findViewById(R.id.change_pwd_new2);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.change_pwd_back:
			finish();
			break;
		case R.id.change_password_ok:
			if (oldPwd.getText().toString().isEmpty()) {
				oldPwd.setText("");
				Toast.makeText(ChangePwdActivity.this, "ԭ���벻��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
			} else if (newPwd1.getText().toString().isEmpty()) {
				newPwd1.setText("");
				Toast.makeText(ChangePwdActivity.this, "�����벻��Ϊ�գ�",
						Toast.LENGTH_SHORT).show();
			} else if (newPwd1.getText().toString().length() < 6) {
				Toast.makeText(ChangePwdActivity.this, "�����벻������6λ��",
						Toast.LENGTH_SHORT).show();

			} else if (!newPwd1.getText().toString()
					.equals(newPwd2.getText().toString())) {
				newPwd2.setText("");
				Toast.makeText(ChangePwdActivity.this, "������������벻һ�£�",
						Toast.LENGTH_SHORT).show();
			} else {
				BmobUser.updateCurrentUserPassword(oldPwd.getText().toString(),
						newPwd1.getText().toString(), new UpdateListener() {
							//
							// public void onSuccess() {
							// Toast.makeText(ChangePwdActivity.this,
							// "�����޸ĳɹ�", Toast.LENGTH_LONG).show();
							// finish();
							// }
							//
							// public void onFailure(int arg0, String arg1) {
							// if (arg0 == 210) {
							// Toast.makeText(ChangePwdActivity.this,
							// "ԭ���벻��ȷ", Toast.LENGTH_LONG).show();
							// } else {
							// Toast.makeText(ChangePwdActivity.this,
							// "�����޸�ʧ��" + arg1, Toast.LENGTH_LONG)
							// .show();
							// }
							// // Log.e("===�����޸�ʧ��===", "�����룺" + arg0 + "�������ݣ�"
							// // + arg1);
							// }

							@Override
							public void done(BmobException e) {
								if (e == null) {
									Toast.makeText(ChangePwdActivity.this,
											"�����޸ĳɹ�", Toast.LENGTH_LONG).show();
									finish();
								} else {
									if (e.getErrorCode() == 210) {
										Toast.makeText(ChangePwdActivity.this,
												"ԭ���벻��ȷ", Toast.LENGTH_LONG)
												.show();
									} else {
										Toast.makeText(ChangePwdActivity.this,
												"�����޸�ʧ��" + e.getErrorCode(),
												Toast.LENGTH_LONG).show();
									}
								}

							}
						});
			}
			break;
		}
	}

}
