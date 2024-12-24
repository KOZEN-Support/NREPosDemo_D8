package com.xc.apex.nre.posdemo.view;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.xc.apex.nre.posdemo.R;
import com.xc.apex.nre.posdemo.databinding.ActivityMainTabBinding;
import com.xc.apex.nre.posdemo.view.base.BaseActivity;
import com.xc.apex.nre.posdemo.view.fragment.ChargeFragment;
import com.xc.apex.nre.posdemo.view.fragment.ItemsFragment;
import com.xc.apex.nre.posdemo.view.fragment.ReceiptsFragment;
import com.xc.apex.nre.posdemo.view.fragment.SalesFragment;
import com.xc.apex.nre.posdemo.view.fragment.SettingsFragment;
import com.xc.apex.nre.posdemo.view.widget.CustomTabView;

public class MainTabActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainTabActivity";

    private ActivityMainTabBinding binding;
    private SalesFragment salesFragment;
    private ReceiptsFragment receiptsFragment;
    private ItemsFragment itemsFragment;
    private SettingsFragment settingsFragment;
    private ChargeFragment chargeFragment;
    private Fragment currentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main_tab);
        binding.setPresenter(this);

        salesFragment = new SalesFragment();
        receiptsFragment = new ReceiptsFragment();
        itemsFragment = new ItemsFragment();
        settingsFragment = new SettingsFragment();

        binding.tabSales.setTabSelected();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, salesFragment).commit();
        currentFragment = salesFragment;
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.tab_sales) {
            switchTabFragment(binding.tabSales, salesFragment);
        } else if (viewId == R.id.tab_receipts) {
            switchTabFragment(binding.tabReceipts, receiptsFragment);
        } else if (viewId == R.id.tab_items) {
            switchTabFragment(binding.tabItems, itemsFragment);
        } else if (viewId == R.id.tab_setting) {
            switchTabFragment(binding.tabSetting, settingsFragment);
        }
    }

    private void switchTabFragment(CustomTabView view, Fragment fragment) {
        if (!view.isTabSelected()) {
            view.setTabSelected();
            switchFragment(fragment);
        }
    }

    public void switchFragment(Fragment fragment) {
        if (currentFragment != fragment) {
            if (!fragment.isAdded()) {
                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).hide(currentFragment).commit();
            } else {
                getSupportFragmentManager().beginTransaction().hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        }
    }

    public void showChargeFragment(Bundle args) {
        chargeFragment = new ChargeFragment();
        chargeFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, chargeFragment).hide(currentFragment).commit();
        currentFragment = chargeFragment;
    }

    public void hideChargeFragment() {
        getSupportFragmentManager().beginTransaction().hide(currentFragment).show(salesFragment).commit();
        currentFragment = salesFragment;
        chargeFragment = null;
    }
}
