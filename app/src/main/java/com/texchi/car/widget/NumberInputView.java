package com.texchi.car.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by ykj on 17/7/13.
 */
public class NumberInputView extends Gallery {

    private static final String TAG = "NumberInputView";
    private NumberInputAdapter adapter;
    private CommandListener commandListener;
    private Handler handler = new Handler();
    private int clickTime = 0;
    private int fling = 0;

    public NumberInputView(Context context) {
        this(context, null);
    }

    public NumberInputView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.galleryStyle);
    }

    public NumberInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        adapter = new NumberInputAdapter(context);
        setAdapter(adapter);
        setSelection(adapter.size() * 1000);
        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NumModel model = (NumModel) adapter.getItem(position);
                if(commandListener != null) {
                    commandListener.onCommand(model.getCommand());
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode != KeyEvent.KEYCODE_DPAD_LEFT && keyCode != KeyEvent.KEYCODE_DPAD_RIGHT){
            return super.onKeyDown(keyCode, event);
        }
        clickTime++;
        handler.postDelayed(runnable, 200);
        if(clickTime <= 2) {
            keyDownRun.keycode = keyCode;
            keyDownRun.keyEvent = event;
            handler.postDelayed(keyDownRun, 50);
            return true;
        }
        handler.removeCallbacks(keyDownRun);
        MotionEvent downEvent = MotionEvent.obtain(0,0,MotionEvent.ACTION_DOWN, 0, 0, 0);
        MotionEvent upEvent = MotionEvent.obtain(0,0,MotionEvent.ACTION_UP, 0, 0, 0);
        if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            fling = 1;
            onFling(downEvent, upEvent, 300f, 0f);
        }else {
            fling = 2;
            onFling(downEvent, upEvent, -300f, 0f);
        }
        downEvent.recycle();
        upEvent.recycle();
        return true;
    }

    KeyDownRun keyDownRun = new KeyDownRun();

    class KeyDownRun implements Runnable {
        public int keycode;
        public KeyEvent keyEvent;

        @Override
        public void run() {
            NumberInputView.super.onKeyDown(keycode, keyEvent);
        }
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            clickTime = 0;
            if(fling == 1) {
                NumberInputView.super.onKeyDown(KeyEvent.KEYCODE_DPAD_LEFT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_LEFT));
            }else if(fling == 2) {
                NumberInputView.super.onKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT, new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DPAD_RIGHT));
            }
            fling = 0;
        }
    };

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    public interface CommandListener {
        void onCommand(String command);
    }

    // Adapter
    public static class NumberInputAdapter extends BaseAdapter {

        private Context context;
        private LayoutInflater mInflater = null;
        private List<NumModel> data;

        public NumberInputAdapter(Context context) {
            this.context = context;
            this.mInflater = LayoutInflater.from(context);
            this.data = new ArrayList<>();
            setData();
        }

        private void setData() {
            List<NumModel> list = new ArrayList<>();
            list.add(new NumModel(NumberInput.ZERO));
            list.add(new NumModel(NumberInput.ONE));
            list.add(new NumModel(NumberInput.TWO));
            list.add(new NumModel(NumberInput.THREE));
            list.add(new NumModel(NumberInput.FOUR));
            list.add(new NumModel(NumberInput.FIVE));
            list.add(new NumModel(NumberInput.SIX));
            list.add(new NumModel(NumberInput.SEVEN));
            list.add(new NumModel(NumberInput.EIGHT));
            list.add(new NumModel(NumberInput.NINE));
            list.add(new NumModel(R.drawable.keyboard_delete_normal, R.drawable.keyboard_delete_selected));
            list.add(new NumModel(NumberInput.OK));
            addAll(list);
        }

        public boolean addAll(Collection<? extends NumModel> collection) {
            data.clear();
            return data.addAll(collection);
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        public int size() {
            return this.data.size();
        }

        @Override
        public Object getItem(int position) {
            int size = data.size();
            return data.get(position % size);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            //如果缓存convertView为空，则需要创建View
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.number_item, null);
                convertView.setFocusable(true);
                holder = new ViewHolder(convertView);
                //将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
                convertView.setTag(holder);
                final int finalPos = position;
                convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        ViewHolder holder = (ViewHolder) v.getTag();

                        NumModel model = data.get(finalPos % size());
                        String number = holder.number.getText().toString();
                        if (hasFocus) {
                            if("".equals(number)) {
                                holder.image.setImageResource(model.getResImgSelected());
                                holder.image.setScaleX(1);
                                holder.image.setScaleY(1);
                            }else {
                                holder.number.setTextColor(context.getResources().getColor(R.color.value_text_color));
                                holder.number.setTextSize(context.getResources().getDimension(R.dimen.t_number_selected_text_size));
                            }
                        } else {
                            if("".equals(number)) {
                                holder.image.setImageResource(model.getResImgNormal());
                                holder.image.setScaleX(0.8f);
                                holder.image.setScaleY(0.8f);
                            }else {
                                holder.number.setTextColor(context.getResources().getColor(R.color.number_input_text_normal));
                                holder.number.setTextSize(context.getResources().getDimension(R.dimen.t_number_unselected_text_size));
                            }
                        }
                    }
                });
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String number = data.get(position % size()).getNumber();
            if(number != null) {
                holder.number.setText(number);
                holder.number.setTextColor(context.getResources().getColor(R.color.number_input_text_normal));
                holder.number.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.GONE);
            }else {
                holder.image.setImageResource(data.get(position % size()).getResImgNormal());
                holder.image.setScaleX(0.8f);
                holder.image.setScaleY(0.8f);
                holder.number.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
            }

            return convertView;
        }

        public static class ViewHolder {

            public TextView number;
            public ImageView image;

            public ViewHolder(View itemView) {
                number = (TextView) itemView.findViewById(R.id.number);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }

    // Model
    public static class NumModel {
        private String number;
        private int resImgNormal;
        private int resImgSelected;
        private String command;

        public NumModel(int resImgNormal, int resImgSelected) {
            this.resImgNormal = resImgNormal;
            this.resImgSelected = resImgSelected;
            this.command = NumberInput.BACK;
        }

        public NumModel(String number) {
            this.number = number;
            this.command = number;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public int getResImgNormal() {
            return resImgNormal;
        }

        public void setResImgNormal(int resImgNormal) {
            this.resImgNormal = resImgNormal;
        }

        public int getResImgSelected() {
            return resImgSelected;
        }

        public void setResImgSelected(int resImgSelected) {
            this.resImgSelected = resImgSelected;
        }

        public String getCommand() {
            return command;
        }

        public void setCommand(String command) {
            this.command = command;
        }
    }

    public static class NumberInput {
        public static String ZERO = "0";
        public static String ONE = "1";
        public static String TWO = "2";
        public static String THREE = "3";
        public static String FOUR = "4";
        public static String FIVE = "5";
        public static String SIX = "6";
        public static String SEVEN = "7";
        public static String EIGHT = "8";
        public static String NINE = "9";
        public static String OK = "OK";
        public static String BACK = "BACK";
    }
}
