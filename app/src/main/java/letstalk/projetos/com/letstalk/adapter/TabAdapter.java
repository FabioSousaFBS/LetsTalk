package letstalk.projetos.com.letstalk.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import letstalk.projetos.com.letstalk.fragment.FragmentContatos;
import letstalk.projetos.com.letstalk.fragment.FragmentConversas;

public class TabAdapter extends FragmentPagerAdapter {

    private String[] tabTitulos;

    public TabAdapter(FragmentManager fm, String[] tabTitulos) {
        super(fm);

        this.tabTitulos = tabTitulos;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new FragmentConversas();
            case 1:
                return new FragmentContatos();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return this.tabTitulos.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.tabTitulos[position];
    }
}
