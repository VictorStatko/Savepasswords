package com.statkovit.personalAccountsService.constants;

public final class MappingConstants {
    public static final String INTERNAL_API_ROUTE = "/internal-api/v1/";
    public static final String EXTERNAL_API_ROUTE = "/external-api/v1/";

    public static final String UUID_PATH = "{uuid}";

    public static final class PersonalAccountsExternalController {
        public static final String CONTROLLER_ROUTE = EXTERNAL_API_ROUTE + "accounts";

        public static final String CREATE_ROUTE = CONTROLLER_ROUTE;
        public static final String UPDATE_ROUTE = CONTROLLER_ROUTE + "/" + UUID_PATH;
        public static final String DELETE_ROUTE = CONTROLLER_ROUTE + "/" + UUID_PATH;
        public static final String GET_LIST_ROUTE = CONTROLLER_ROUTE;
        public static final String GET_LIST_COUNT_ROUTE = CONTROLLER_ROUTE + "/count";
    }

    public static final class FoldersExternalController {
        public static final String CONTROLLER_ROUTE = EXTERNAL_API_ROUTE + "folders";

        public static final String CREATE_ROUTE = CONTROLLER_ROUTE;
        public static final String UPDATE_ROUTE = CONTROLLER_ROUTE + "/" + UUID_PATH;
        public static final String GET_LIST_ROUTE = CONTROLLER_ROUTE;
    }
}
