package makeinbvb.com.mibofficialapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ViewPagerAdapter_AboutMiB extends PagerAdapter
{
    private Context ctx;
    private LayoutInflater layoutInflater;
    private Integer [] img = {R.drawable.butterfly, R.drawable.esummit_img, R.drawable.intel_ideation_img};



    public ViewPagerAdapter_AboutMiB(Context ctx)
    {
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        layoutInflater = (LayoutInflater)ctx.getSystemService(ctx.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.custom_layout_about_mib,null);
        ImageView imageView = view.findViewById(R.id.img_view_about_us);

        Picasso.get().load(img[position]).fit().into(imageView);

        ViewPager viewPager = (ViewPager)container;

        viewPager.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager viewPager = (ViewPager)container;
        View view = (View)object;
        viewPager.removeView(view);
    }
}
