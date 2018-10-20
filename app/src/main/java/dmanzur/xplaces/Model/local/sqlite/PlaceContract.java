package dmanzur.xplaces.Model.local.sqlite;

public final class PlaceContract {

    private PlaceContract(){}



    public static class PlaceEntry   {

        public static final String TABLE_NAME = "place_info";
        public static final String PLACE_ID = "place_id";
        public static final String NAME = "name";
        public static final String COUNTRY = "country";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String FAVORITE = "favorite";
        public static final String DESCRIPTION = "description";

    }

}
