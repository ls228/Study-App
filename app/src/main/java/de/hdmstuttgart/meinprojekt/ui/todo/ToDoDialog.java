
package de.hdmstuttgart.meinprojekt.ui.todo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdmstuttgart.meinprojekt.R;
import de.hdmstuttgart.meinprojekt.model.todo.ToDoItem;

public class ToDoDialog extends android.app.DialogFragment {

        private static final String TAG = "Dialog";
        private String inputTitle ="";
        private String inputTopic ="";
        List<ToDoItem> list = new ArrayList<>();


        private EditText titleInput;
        private EditText topicInput;
        private TextView mActionAdd, mActionCancel;


        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container,
                                 Bundle savedInstanceState)
        {

            View view = inflater.inflate(R.layout.addtodo_dialog, container, false);

            mActionCancel = view.findViewById(R.id.btncancel);
            mActionAdd = view.findViewById(R.id.btncancel);
            titleInput = view.findViewById(R.id.titleinput);
            topicInput = view.findViewById(R.id.textInputLayout);

            mActionCancel.setOnClickListener(
                    v -> {
                        //Log.d(TAG, "onClick: closing dialog");
                        getDialog().dismiss();
                    });

            mActionAdd.setOnClickListener(
                    v -> {
                        //Log.d(TAG, "onClick: capturing input");
                        inputTitle = titleInput.toString();
                        inputTopic = topicInput.toString();
                        getDialog().dismiss();
                    });


            return view;
        }

        @Override public void onAttach(Context context)
        {
            super.onAttach(context);
            try {
                list.add(new ToDoItem(inputTitle,Calendar.getInstance().getTime(),inputTopic));
            }
            catch (ClassCastException e) {
                Log.e(TAG, "onAttach: ClassCastException: "
                        + e.getMessage());
            }
        }
    }

    /*

    Button btn = dialogView.findViewById(R.id.button);
                    btn.setOnClickListener(a -> {
        TextView titleInput = dialogView.findViewById(R.id.titleinput);
        title = String.valueOf(titleInput.getText());

    }

                    );
                    System.out.println(title);
}*/
