package com.lh.daily.widget;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.lh.daily.DailyApp;
import com.lh.daily.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by home on 2017/2/11.
 */

public class SearchView extends DialogFragment implements View.OnClickListener {

    private View mBack;
    private View mSearch;
    private EditText mEditText;
    private RecyclerView mRecyclerView;

    private SuggestAdapter mAdapter;

    private List<String> mSuggests = new ArrayList<>();

    private Disposable mDisposable;

    private OnSearchListener mOnSearchListener;

    public static SearchView newInstance() {
        return new SearchView();
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.mOnSearchListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.SearchTheme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_view, container, false);
        mBack = view.findViewById(R.id.back);
        mSearch = view.findViewById(R.id.search);
        mEditText = (EditText) view.findViewById(R.id.edit_text);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getDialog().getWindow();
        if (window != null) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBack.setOnClickListener(this);
        mSearch.setOnClickListener(this);
        if (mAdapter == null) {
            mAdapter = new SuggestAdapter();
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mAdapter);
        initEditText();
    }

    public void setSuggest(List<String> suggests) {
        mSuggests.clear();
        if (suggests != null && !suggests.isEmpty()) {
            mSuggests.addAll(suggests);
            mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
        }
        if (mAdapter == null) {
            return;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initEditText() {
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch();
                }
                return false;
            }
        });
        mDisposable = Observable
                .create(new ObservableOnSubscribe<String>() {
                    @Override
                    public void subscribe(ObservableEmitter<String> e) throws Exception {
                        final WeakReference<ObservableEmitter<String>> wrappedEmitter = new WeakReference<>(e);
                        mEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                            }

                            @Override
                            public void afterTextChanged(Editable editable) {
                                ObservableEmitter<String> emitter = wrappedEmitter.get();
                                if (emitter == null || emitter.isDisposed()) {
                                    mEditText.removeTextChangedListener(this);
                                    return;
                                }
                                emitter.onNext(editable.toString());
                            }
                        });
                    }
                })
                .debounce(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<String>() {
                    @Override
                    public void onNext(String s) {
                        if (mOnSearchListener != null) {
                            mOnSearchListener.onTextChanged(s);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.container:
            case R.id.back:
                dismiss();
                break;
            case R.id.search:
                performSearch();
                break;
        }
    }

    private void performSearch() {
        if (mOnSearchListener != null) {
            mOnSearchListener.onSearch(mEditText.getText().toString());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        DailyApp.getWatcher(getContext()).watch(this);
    }

    public interface OnSearchListener {
        void onSuggestClick(int index);

        void onSearch(String key);

        void onTextChanged(String after);
    }

    class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.SuggestHolder> {
        @Override
        public SuggestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            int padding = getResources().getDimensionPixelOffset(R.dimen.item_spacing);
            textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(padding, padding, padding, padding);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimensionPixelSize(R.dimen.text_size_large));
            textView.setMaxLines(1);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            TypedValue t = new TypedValue();
            getContext().getTheme().resolveAttribute(R.attr.selectableItemBackground, t, true);
            textView.setBackgroundResource(t.resourceId);
            return new SuggestHolder(textView);
        }

        @Override
        public void onBindViewHolder(SuggestHolder holder, int position) {
            holder.textView.setText(mSuggests.get(position));
        }

        @Override
        public int getItemCount() {
            return mSuggests.size();
        }

        class SuggestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView textView;

            SuggestHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (mOnSearchListener != null) {
                    mOnSearchListener.onSuggestClick(getAdapterPosition());
                }
            }
        }
    }
}
