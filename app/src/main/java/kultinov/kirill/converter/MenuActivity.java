package kultinov.kirill.converter;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.crossfader.util.UIUtils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

/**
 * MenuActivity is the main activity of the application
 * It is used to populate a menu items in the navigation drawer
 * using mikepenz's library.
 * MenuActiviy us used to handle transactions among fragments of the app and
 * replace contents of the FrameLayout.
 */

public class MenuActivity extends AppCompatActivity {

    public FrameLayout container_converter;
    TextView toolbarText;

    private MiniDrawer miniResult = null;
    Drawer result = null;
    Crossfader crossFader;
    AccountHeader header;

    public FrameLayout converter_field;
    FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //initialize containers of the main activity
        container_converter = (FrameLayout)findViewById(R.id.container_converter_field);// fragment template for displaying all sections of the converter
        toolbarText = (TextView) findViewById(R.id.toolbar_text);
        toolbarText.setText("Temperature");
        converter_field = (FrameLayout) findViewById(R.id.container_converter_field);

        //initialize fragments and a fragment transaction
        fm = getFragmentManager();
        final Fragment temperatureFragment = new TemperatureFragment();
        final Fragment speedFragment = new SpeedFragment();
        final Fragment timeFragment = new TimeFragment();
        final Fragment weightFragment = new WeightFragment();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();


        /**the code below is used to build third party navigation drawer along with handling onItemClick listeners
         * of the menu drawer items
         */

        //create menu items for the custom navigation drawer
        PrimaryDrawerItem temperature = new PrimaryDrawerItem().withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_ac_unit).withName("Temperature").withSelectable(true).withSelectedIconColorRes(R.color.colorAccent);
        PrimaryDrawerItem speed = new PrimaryDrawerItem().withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_motorcycle).withName("Speed").withSelectable(true).withSelectedIconColorRes(R.color.colorAccent);
        PrimaryDrawerItem time = new PrimaryDrawerItem().withIdentifier(3).withIcon(GoogleMaterial.Icon.gmd_schedule).withName("Time").withSelectable(true).withSelectedIconColorRes(R.color.colorAccent);
        PrimaryDrawerItem weight = new PrimaryDrawerItem().withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_fitness_center).withName("Weight").withSelectable(true).withSelectedIconColorRes(R.color.colorAccent);

        //build navigation drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(false)
                .addDrawerItems(
                        temperature,
                        new DividerDrawerItem(),
                        speed,
                        new DividerDrawerItem(),
                        time,
                        new DividerDrawerItem(),
                        weight,
                        new DividerDrawerItem()

                )

                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    /**
                     * function is used to handle transactions between fragments when a new drawer item
                     * is clicked
                     */
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        final FragmentTransaction transaction1 = getFragmentManager().beginTransaction();
                        if (drawerItem.getIdentifier() == 1) {
                            toolbarText.setText("Temperature");
                            transaction1.replace(R.id.container_converter_field, temperatureFragment);
                            transaction1.addToBackStack(null);
                            transaction1.commit();
                        } else if (drawerItem.getIdentifier() == 2) {
                            toolbarText.setText("Speed");
                            transaction1.replace(R.id.container_converter_field, speedFragment);
                            transaction1.addToBackStack(null);
                            transaction1.commit();

                        } else if (drawerItem.getIdentifier() == 3) {
                            toolbarText.setText("Time");
                            transaction1.replace(R.id.container_converter_field, timeFragment);
                            transaction1.addToBackStack(null);
                            transaction1.commit();

                        } else if (drawerItem.getIdentifier() == 4) {
                            toolbarText.setText("Weight");
                            transaction1.replace(R.id.container_converter_field, weightFragment);
                            transaction1.addToBackStack(null);
                            transaction1.commit();

                        }
                        return false;
                    }
                }).withGenerateMiniDrawer(true)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(1) // temperature converter section is chosen by default
                .buildView();
        //show TemperatureFragment
        transaction.replace(R.id.container_converter_field, temperatureFragment);
        transaction.addToBackStack(null);
        transaction.commit();

        result.openDrawer();
        //get the widths in px for the first and second panel
        int firstWidth = (int) UIUtils.convertDpToPixel(250, this);
        int secondWidth = (int) UIUtils.convertDpToPixel(65, this);
        miniResult = result.getMiniDrawer();
        //set cross fader that is used for displaying icons of each drawer item even when the navigation drawer is closed
        crossFader = new Crossfader()
                .withContent(findViewById(R.id.drawerlayout))
                .withFirst(result.getSlider(), firstWidth)
                .withSecond(miniResult.build(this), secondWidth)
                .withSavedInstance(savedInstanceState)
                .build();
        miniResult.withCrossFader(new CrossfadeWrapper(crossFader));

        crossFader.getCrossFadeSlidingPaneLayout().setShadowResourceLeft(R.drawable.material_drawer_shadow_left);

    }
}
