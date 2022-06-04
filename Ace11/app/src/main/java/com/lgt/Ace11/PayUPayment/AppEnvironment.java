package com.lgt.Ace11.PayUPayment;

public enum AppEnvironment {

    SANDBOX {
        @Override
        public String merchant_Key() {
            return "UroZi1Vw";
        }

        @Override
        public String merchant_ID() {
            return "7344937";
        }

        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "domeiKIJeC";
        }

        @Override
        public boolean debug() {
            return false;
        }
    },
    PRODUCTION {
        @Override
        public String merchant_Key() {
            return "UroZi1Vw";
        }
        @Override
        public String merchant_ID() {
            return "7344937";
        }
        @Override
        public String furl() {
            return "https://www.payumoney.com/mobileapp/payumoney/failure.php";
        }

        @Override
        public String surl() {
            return "https://www.payumoney.com/mobileapp/payumoney/success.php";
        }

        @Override
        public String salt() {
            return "domeiKIJeC";
        }

        @Override
        public boolean debug() {
            return false;
        }
    };

    public abstract String merchant_Key();

    public abstract String merchant_ID();

    public abstract String furl();

    public abstract String surl();

    public abstract String salt();

    public abstract boolean debug();

}
