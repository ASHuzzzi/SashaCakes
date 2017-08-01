package ru.lizzzi.sashacakes;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ru.lizzzi.sashacakes.data.ItemConract.ItemList;
import ru.lizzzi.sashacakes.data.ItemDbHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Delivery.OnFragmentInteractionListener,
        MenuFragment.OnListFragmentInteractionListener, AboutMe.OnFragmentInteractionListener,
        Basket.OnFragmentInteractionListener, Contact.OnFragmentInteractionListener,
        ItemFragment.OnFragmentInteractionListener {

    public BottomNavigationView mBottomNav;
    private Integer quantity = 0; //всего количество всех позиций в заказе для badge
    private ItemDbHelper mDbHelper;
    final String LOG_TAG = "SQLite";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDbHelper = new ItemDbHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mBottomNav = (BottomNavigationView) findViewById(R.id.navigation);

        final BottomNavigationItem item1 = new BottomNavigationItem(R.string.Call, R.drawable.call, R.drawable.call);
        final BottomNavigationItem item2 = new BottomNavigationItem(R.string.Menu, R.drawable.menu, R.drawable.menu);
        final BottomNavigationItem item3 = new BottomNavigationItem(R.string.Basket, R.drawable.basket_menu,  R.drawable.basket_menu);

        final List<BottomNavigationItem> list = new ArrayList<>();
        list.add(item1);
        list.add(item2);
        list.add(item3);

        mBottomNav.addItems(list);
        setTitle(R.string.app_name);
        final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mBottomNav.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationViewBehavior());
        BadgeShow ();

        mBottomNav.setNavigationItemSelectListener(new OnNavigationItemSelectListener() {
            @Override
            public void onSelected(BottomNavigationView bottomNavigationView, View itemView, int position) {
                bottomNavigationView.setBadgeViewNumberByPosition(position, 0);

                Fragment fragment = null;
                Class fragmentClass = null;
                if (position == 0){
                    fragmentClass = Contact.class;
                    layoutParams.setBehavior(new BottomNavigationViewBehavior());
                }
                if (position == 1){
                    fragmentClass = MenuFragment.class;
                    layoutParams.setBehavior(new BottomNavigationViewBehavior());
                }
                if (position == 2){
                    fragmentClass = Basket.class;
                    BadgeShow ();
                    layoutParams.setBehavior(new BottomNavigationViewBehavior_NH());
                }
                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.replace(R.id.content_main, fragment);
                ft.addToBackStack(null);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }
        });

        Fragment fragment = null;
        Class fragmentClass = null;
        fragmentClass = MenuFragment.class;
        FragmentManager fragmentManager = getSupportFragmentManager();
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.commit();

        ChekdbonItem();

    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Настройки
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        Fragment fragment = null;
        Class fragmentClass = null;

        int id = item.getItemId();

        if (id == R.id.delivery) {
            fragmentClass = Delivery.class;
        } else if (id == R.id.about_me) {
            fragmentClass = AboutMe.class;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_main, fragment);
        ft.addToBackStack(null);
        ft.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    public void BadgeShow () {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        quantity = 0;
        Cursor cursor = db.query(ItemList.TABLE_NAME,
                new String[] {"SUM(" + ItemList.COLUMN_QUANTITY + ")"},
                null, null, null, null, null);
        if (cursor.moveToFirst()) {
            quantity = cursor.getInt(0);
        }

        mBottomNav.setBadgeViewNumberByPosition(2,quantity);
        cursor.close();
    }

    private void InsertItem() {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ItemList.COLUMN_NAME, "Капкейк");
        values.put(ItemList.COLUMN_ABOUT, "Капкейк — это, буквально, чашечный торт (cup cake). Миниатюрный тортик на несколько укусов. В его основе лежит нежнейший бисквит, яркая домашняя начинка, а завершает нежнейшая \"шапка\" из воздушного сырно-сливочного крема и свежих ягод. Капкейки — это приятный подарок близкому и прекрасное дополнение к любому праздничному столу. Капкейки — это идеальное решение, как отдельное пирожное каждому гостю, так и дополнение к праздничному торту, выполненному в одной цветовой гамме. Про капкейки можно сказать: \"все гениальное — просто\"!");
        values.put(ItemList.COLUMN_PRICE, "150");
        long newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Торт");
        values.put(ItemList.COLUMN_ABOUT, "Торт, сделанный на заказ, станет неотъемлемой частью Вашего праздника, он послужит украшением стола, произведет впечатление и останется в памяти дорогих гостей. Каждый торт — это индивидуальная работа, обговоренная до мелочей. Вам понравится не только его наружное оформление, но и внутренние богатый мир. При выполнении заказа, мы учитываем все Ваши пожелания, находим лучшие сочетания, добавляем нотку креатива и щедрую горсть нашей любви. Каждый торт, произведенный для Вас, сделан с душой, отдавая его Вам, мы волнуемся не меньше Вашего, чтобы он понравился абсолютно всем и каждому. Мы не просто печем сладости, мы делаем Ваш праздник незабываемым!");
        values.put(ItemList.COLUMN_PRICE, "1600");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Детский торт");
        values.put(ItemList.COLUMN_ABOUT, "Торт, сделанный на заказ, станет неотъемлемой частью Вашего праздника, он послужит украшением стола, произведет впечатление и останется в памяти дорогих гостей. Каждый торт — это индивидуальная работа, обговоренная до мелочей. Вам понравится не только его наружное оформление, но и внутренние богатый мир. При выполнении заказа, мы учитываем все Ваши пожелания, находим лучшие сочетания, добавляем нотку креатива и щедрую горсть нашей любви. Каждый торт, произведенный для Вас, сделан с душой, отдавая его Вам, мы волнуемся не меньше Вашего, чтобы он понравился абсолютно всем и каждому. Мы не просто печем сладости, мы делаем Ваш праздник незабываемым!");
        values.put(ItemList.COLUMN_PRICE, "1600");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Чизкейк");
        values.put(ItemList.COLUMN_ABOUT, "Воздушный, нежный, тает во рту, как мороженое, при этом абсолютно не приторный, с тонкой ноткой кислинки. В нем 60  творога и 30 домашнего йогурта! И почти нет сахара. Тончайшая песочная основа идеально дополняет это белое воздушное облако счастья!");
        values.put(ItemList.COLUMN_PRICE, "2100");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Торт \"Птичье молоко\"");
        values.put(ItemList.COLUMN_ABOUT, "Торт с удивительным названием «Птичье молоко» — любимый сладкий десерт из детства. Кто из нас не любил откусывать маленькими кусочками нежное белое суфле, покрытое вкуснейшим шоколадом?");
        values.put(ItemList.COLUMN_PRICE, "1600");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Ярусный торт");
        values.put(ItemList.COLUMN_ABOUT, "Торт, сделанный на заказ, станет неотъемлемой частью Вашего праздника, он послужит украшением стола, произведет впечатление и останется в памяти дорогих гостей. Каждый торт — это индивидуальная работа, обговоренная до мелочей. Вам понравится не только его наружное оформление, но и внутренние богатый мир. При выполнении заказа, мы учитываем все Ваши пожелания, находим лучшие сочетания, добавляем нотку креатива и щедрую горсть нашей любви. Каждый торт, произведенный для Вас, сделан с душой, отдавая его Вам, мы волнуемся не меньше Вашего, чтобы он понравился абсолютно всем и каждому. Мы не просто печем сладости, мы делаем Ваш праздник незабываемым!");
        values.put(ItemList.COLUMN_PRICE, "1600");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Медовик");
        values.put(ItemList.COLUMN_ABOUT, "Вкусный, нежный, ароматный и совсем не приторный десерт, который с удовольствием отведают даже тот, кто не любит мед. Торт Медовый – мечта, которая легко сбывается при помощи несложных манипуляций и нескольких простых ингредиентов. Главным героем здесь выступает мед – полезный, вкусный и лечебный продукт. Его нужно добавить в тесто совсем немного — всего лишь пару столовых ложек, а результат потрясающий.");
        values.put(ItemList.COLUMN_PRICE, "2100");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Зефир");
        values.put(ItemList.COLUMN_ABOUT, "Зефир — не только аппетитное, но еще и полезное для здоровья лакомство. Польза и вред зефира оценена не только сладкоежками, но также и специалистами-диетологами. Его можно без опаски давать детям — от его употребления у них не будут разрушаться зубы и не возникнет кариес.");
        values.put(ItemList.COLUMN_PRICE, "450");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Кейк-попсы");
        values.put(ItemList.COLUMN_ABOUT, "Что это за чудо, скажите вы? Кейк-попс- это лакомство на палочке, которое понравится абсолютно всем, равнодушных не останется точно. Кейк-попсы идеальная сладость для детского и свадебного кэнди-бара. Шоколадная или ванильная основа по типу нашей любимой \"Картошки\", окутанная в благородный бельгийский шоколад и красиво оформлена цветными посыпками на любой цвет и вкус. Кейк-попс -это все и сразу в одном удобном решении");
        values.put(ItemList.COLUMN_PRICE, "80");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Вафельные трубочки");
        values.put(ItemList.COLUMN_ABOUT, "Вафельные трубочки — рецепт, который мы любим с детства. Ради вафельных трубочек даже был придуман специальный агрегат — электровафельница, и написан специальный рецепт вафельных трубочек для электровафельницы.");
        values.put(ItemList.COLUMN_PRICE, "80");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Пирожное \"Картошка\"");
        values.put(ItemList.COLUMN_ABOUT, "Я делаю пирожное \"картошка\" не по классическому рецепту, а оочень шоколадному, насыщенному! Вы бы знали какой аромат исходит от коробочки с ними! И вообще это прекрасный подарок близкому!");
        values.put(ItemList.COLUMN_PRICE, "100");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);

        values.put(ItemList.COLUMN_NAME, "Торт для мужчины");
        values.put(ItemList.COLUMN_ABOUT, "Если Вы думаете, что сильная половина человечества не любит сладкого, Вас сильно ввели в заблуждение! Мало того, что мужчины еще те сладкоежки, они еще и очень требовательны в этом вопросе. Тут нужно сильно постараться, чтобы внутреннее богатство торта совпало с внешним сдержанным украшением и произвело впечатление. Если у Вас есть свои идеи-мы обязательно их воплотим, если понравились наши работы- выполним их с большим удовольствием, учтем все пожелания и непременно добавим \"перчинки\" для именинника");
        values.put(ItemList.COLUMN_PRICE, "1600");
        newRowId = db.insert(ItemList.TABLE_NAME, null, values);
        Log.d(LOG_TAG, "row inserted, ID = " + newRowId);
    }

    private  void ChekdbonItem(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ItemList._ID
        };

        Cursor cursor = db.query(
                ItemList.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        //Toast.makeText(this, "Кол-во позицийв таблице " + cursor.getCount(), Toast.LENGTH_SHORT).show();

        if (cursor.getCount()<12){
            InsertItem();
        }
        cursor.close();
    }

}
