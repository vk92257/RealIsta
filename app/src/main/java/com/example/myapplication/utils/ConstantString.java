package com.example.myapplication.utils;

public class ConstantString {


    //Chat settings
    public static final String USER_TAG = "usertag";
    public static final String DIALOG_EXTRA = "messagedialog";
    public static final String USER_DEFAULT_PASSWORD = "123456789";
    public static final int CHAT_PORT = 5223;
    public static final int SOCKET_TIMEOUT = 300;
    public static final boolean KEEP_ALIVE = true;
    public static final boolean USE_TLS = true;
    public static final boolean AUTO_JOIN = false;
    public static final boolean AUTO_MARK_DELIVERED = true;
    public static final boolean RECONNECTION_ALLOWED = true;
    public static final boolean ALLOW_LISTEN_NETWORK = true;
    public static final String EXTRA_FCM_MESSAGE = "message";
    public static final String ACTION_NEW_FCM_EVENT = "new-push-event";
    //    chatting intent tags
    public static final String IMG_USER = "profileimage";
    public static final String CHAT_RCV_USER = "reciver_user";
    public static final String  CHAT_IMAGE_VIEW="viewchatimage";
    public static final String ATTACHMENT_TYPE="attachmenttype";



    //    Calling settings
    public static final int ERR_LOGIN_ALREADY_TAKEN_HTTP_STATUS = 422;

    public static final int MAX_OPPONENTS_COUNT = 6;
    public static final int MAX_LOGIN_LENGTH = 15;
    public static final int MAX_FULLNAME_LENGTH = 20;

    public static final String EXTRA_QB_USER = "qb_user";
    public static final String EXTRA_USER_ID = "user_id";
    public static final String EXTRA_USER_LOGIN = "user_login";
    public static final String EXTRA_USER_PASSWORD = "user_password";
    public static final String EXTRA_PENDING_INTENT = "pending_Intent";
    public static final String EXTRA_CONTEXT = "context";
    public static final String EXTRA_OPPONENTS_LIST = "opponents_list";
    public static final String EXTRA_CONFERENCE_TYPE = "conference_type";

    public static final String EXTRA_IS_INCOMING_CALL = "conversation_reason";

    public static final String EXTRA_LOGIN_RESULT = "login_result";
    public static final String EXTRA_LOGIN_ERROR_MESSAGE = "login_error_message";
    public static final int EXTRA_LOGIN_RESULT_CODE = 1002;

    //    Selection return code
    public static final int LANGCODE = 1001;
    public static final int SKILLCODE = 1002;
    public static final int BODYTYPRCODE = 1003;
    public static final int ETHNICITYCODE = 1004;
    public static final String CODETEXT = "codetext";
    public static final String SELECT_TOP_TXT = "selecttoptxt";
    public static final int PICK_IMAGE_MULTIPLE = 1111;
    public static final int PICK_IMAGE_SINGLE = 2222;
    public static final int HAIRCOLORCODE = 9999;
    public static final int EYECOLORCODE = 1010;
    public static final int HAIRLENGTHCODE = 3131;
    public static final int JOBETYPECODE = 3333;
    public static final int GENDERSELECTIONCODE = 1919;
    public static final int ALLFILEGETCODE = 2929;
    public static final int CodeforExplore = 9812;
    public static final int location_code = 0000;


    public static final String CLIENT_LOGIN = "Client";
    public static final String MODEL_LOGIN = "Talent";

    //    signup Api parameter
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String ROLE = "role";
    public static final String MOBILE_NO = "mobile";

    //    Base response parameter
    public static final String IS_ERROR = "error";
    public static final String RESPONSE_MESSAGE = "message";
    public static final String RESPONSE_SUCCESS = "success";

    //    Login Api parameter
    public static final String TOKEN = "token";
    public static final String ID = "id";
    public static final String CHECK_PROFILE = "checkProfile";
    public static final String PROFILE_IMAGESAVE = "profile_img";

    //    Client PORTFOLIO
    public static final String PHONE_NO = "PhoneNo";
    public static final String COUNTRY_NAME = "CountryName";
    public static final String CURRENT_STATE = "CurrentState";
    public static final String CURRENT_CITY = "currentCity";
    public static final String PROFILE_IMG = "profile_img";
    public static final String GENDER = "gender";

    //    intent strings
    public static final String USERID = "user_id";

    //    Talent Api param
    public static final String AGE = "Age";
    public static final String PROFESSION = "Profession";
    public static final String PASSPORT = "passPort";
    public static final String DETAILBIO = "detailedBio";
    public static final String PRO_JOURNEY = "professionalJourney";
    public static final String HOURLY_RATE = "HourlyRate";
    public static final String APP_BIO = "appearanceBio";
    public static final String APPEAR_PROF = "appearanceProf";
    public static final String MINIMUMAGE = "minimumAge";
    public static final String MAXAGE = "maxAge";
    public static final String HEIGHTFEET = "heightFeet";
    public static final String HEIGHTINCH = "heightInch";
    public static final String WEIGHT = "weight";
    public static final String VIDEOURL = "videoUrl";
    public static final String LANGUAGE = "language";
    public static final String SKILL = "skills";
    public static final String BODY_TYPE = "bodyType";
    public static final String ETHINICITY = "ethinicity";
    public static final String IMAGEHD = "imgHd";
    public static final String GENERMODEL = "Gender";
    public static final String BODY_WAIST = "waist";
    public static final String BODY_HIP = "hip";
    public static final String BODY_CHEST = "chest";

//    get Profile Client

    public static final String DETAIL_TAG = "details";
    public static final String GET_CLIENT_MOBILE = "mobile";
    public static final String GET_CLIENT_COUNTRY = "country";
    public static final String GET_CLIENT_STATE = "state";
    public static final String GET_CLIENT_CITY = "city";
    public static final String GET_CLIENT_CREATED_AT = "created_at";
    public static final String GET_CLIENT_UPDATED_AT = "updated_at";

//    get Profile Talent

    public static final String GET_TALENT_MOBILE = "mobile";
    public static final String GET_TALENT_COUNTRY = "country";
    public static final String GET_TALENT_STATE = "state";
    public static final String GET_TALENT_CITY = "city";
    public static final String GET_TALENT_PROFFESION = "proffesion";
    public static final String GET_TALENT_PASSPORT = "passport";
    public static final String GET_TALENT_PERSONALBIO = "personal_bio";
    public static final String GET_TALENT_PERSONAL_JOURNEY = "personal_journey";
    public static final String GET_TALENT_HOULY_RATE = "hourly_rate";
    public static final String GET_TALENT_BODYBIO = "body_bio";
    public static final String GET_TALENT_BODYJOURN = "body_journey";
    public static final String GET_TALENT_ROLE_AGEMIN = "role_age_min";
    public static final String GET_TALENT_ROLE_AGEMAX = "role_age_max";
    public static final String GET_TALENT_HEIGHT_FEET = "height_feet";
    public static final String GET_TALENT_HEIGHT_INC = "height_inches";
    public static final String GET_TALENT_SKILL = "skill";
    public static final String GET_TALENT_HD_IMAGE = "hd_images";
    public static final String GET_TALENT_WEIGHT = "weight";
    public static final String GET_TALENT_VIDEO_URL = "video_url";
    public static final String GET_TALENT_LANGUAGE = "language";
    public static final String GET_TALENT_BODY_TYPE = "body_type";
    public static final String GET_TALENT_ETHNICITY = "ethnicity";
    public static final String GET_TALENT_CREATEAT = "created_at";
    public static final String GET_TALENT_UPDATEDAT = "updated_at";
    public static final String GET_TALENT_AGE = "age";
    public static final String GET_TAlENT_WAIST = "waist";
    public static final String GET_TALENT_HIP = "hip";
    public static final String GET_TALENT_CHEST = "chest";

//    Add jobs detail

    public static final String ADD_HAIR_COLOR = "hair_color";
    public static final String ADD_EYE_COLOR = "eye_color";
    public static final String ADD_SPECIAL_SKILLS = "special_skills";
    public static final String ADD_ETHINICITY = "ethinicity";
    public static final String ADD_HAIR_LENGTH = "hair_length";
    public static final String ADD_JOB_TYPE = "job_type";
    public static final String ADD_ATTACHMENT = "attachment";
    public static final String ADD_MIN_AGE = "min_age";
    public static final String ADD_MAX_AGE = "max_age";
    public static final String ADD_HEIGHT_FEET = "height_feet";
    public static final String ADD_HEIGHT_INCH = "height_inch";
    public static final String ADD_IDEAL_WEIGTH = "ideal_weight";
    public static final String ADD_ABOUT_PROJECT = "about_project";
    public static final String ADD_ABOUT_PERSONALITY = "about_personality";
    public static final String ADD_HOURLY_RATE = "hourly_rate";
    public static final String ADD_JOBINFO = "jobinfo";
    public static final String ADD_TOTAL_ROLES = "total_roles";
    public static final String ADD_GENDER_TYPE = "gender_type";
    public static final String ADD_PRODUCT_NAME = "product_name";
    public static final String ADD_JOB_DURATION = "job_duration";
    public static final String ADD_ROLE_DESCRIPTION = "role_description";
    public static final String ADD_SHOOT_PERFORMANCE = "shoot_performance";
    public static final String ADD_SHOOT_DATE_START = "shoot_date_start";
    public static final String ADD_SHOOT_DATE_END = "shoot_date_end";
    public static final String ADD_CLIENT_INFO = "client_info";


}


















