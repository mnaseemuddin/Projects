import 'package:get/get.dart';

class Language extends Translations {
  @override
  Map<String, Map<String, String>> get keys => {
    'en_US': {
      'hello': 'Hello World',
      'Language':'Language',
      'English':'English',
      // Forgot password
      'Forgot_Password': 'Forgot Password',
      'Enter_email': 'Enter email',
      'Submit':'Submit',
      'Reset_password':'Reset password',
      'New_password':'New password',
      'Enter_new_password':'Enter new password',
      'Confirm_password':'Confirm password',
      'Password_do_not_matched':'Password do not matched',
      'Please_enter_OTP':'Please enter OTP',
      'Resend_OTP':'Resend OTP',
      'Enter_recv_opt':'Enter OTP that you have received on your registered email @email',
      'Enter_login_password':'Enter login password',
      
      // SystemSettings
      'System_Settings':'System Settings',
      'Contact_Us':'Contact Us',
      'Contact_Us_desc': 'Please contact our customer service if you have any questions',
      'Email_': 'Email: @email',
      'Telegram': 'Telegram: @tel',
      'direct_contact': 'You also can direct @name (Telegram) if you have any issue with trading error code.',
      'Firmware_version':'Firmware version',
      'Valuation_Method':'Valuation Method',
      'Select_language':'Select language',
      'About_name':'About @name',
      'Logout':'Logout',
      // Member Center
      'Member_Center':'Member Center',
      'Renew':'Renew',
      'Remaining':'Remaining: @day days',
      'Rank':'Rank @rank',
      'Star':'@star',
      'Team':'Team @team',
      'VIP_Membership_right':'VIP Membership right',
      'Direct_recommended_award':'Direct recommended award',
      'Robot_activation_direct_push':'Robot activation direct push @activationamount',
      'Team_reward':'Team reward',
      'Profit_distribution_reward_ratio':'Profit distribution reward ratio @rewardratio',
      'Member_use_rules':'Member use rules',
      'Direct_push':'1. Directly push user profit to deduct the positive part of fuel consumption, and '
                    'directly push member to purchase activation code.',
      'final_interp':'2. The final interpretation right belong to the company.',
      'Turn_on_automatic_renewal':'Turn on automatic renewal',
      'Total_amnt_USDT':'Total: @amount USDT',
      'ageed_to':'I have agreed to @name User Activation Service Agreement.',
      'Cancel':'Cancel',

      // Reward List

      'Reward_List':'Reward List',
      'From':'From @user',
      'Remark':'Remark: @remark',
      'Date':'Date: @date',
      'no_member_in_team':'There is no member in your team',

      // Security Center

      'Security_Center':'Security Center',
      'Change_password':'Change password',
      'Modify_email':'Modify email',
      'Transaction_password':'Transaction password',
      'Google_verification_code':'Google verification code',
      'Not_yet_set':'Not yet set',

      // Direct Team

      'Direct_Team':'Direct Team',
      'Name':'Name @name',
      'Username':'Username @name',
      'Active':'Active',
      'In-Active':'In-Active',
      // 
      'Level':'Level: @level',
      'Total':'Total: @total',
      'View':'View',
      // Level

      'Level':'Level: @level',
      // Rank

      'Rank':'Rank: @rank',
      // Teams
      'Direct_Team': 'Direct Team',
      'Level_Team': 'Level Team',
      'Rank_Team':'Rank Team',

      'Total_Team': 'Total Team @team',
        // mine_dashboard
      'VIP':'Welcome',
      'View_permission':'to Jackbot',
      'Commander_Tim':'Commander Tim',
      'Activate':'Activate',
      'Activated':'Activated',
      'Professional_addition':'Professional addition',
      'Member_center':'Member center',
      'Asset':'Asset',
      'API_Binding':'API Binding',
      'Transaction_record':'Transaction record',
      'Billing_Details':'Billing Details',
      'Reward_details':'Team Income',
      'Reward_details_report':'Reward details  report',
      'Community':'Community',

      'Share':'Share',
      'Security_center':'Security center',
      // Group Chat
      'Group_Chat':'Group Chat',
      'Comment':'Comment',
      'Like':'Like',
      'Under_development': 'Under development',
      // currency_details.dart

      'Log':'Log',
      'Position_amount':'Position amount(USDT)',
      'Avg_price':'Avg. price',
      'Number_of_call_margin':'Number of call margin',
      'Position_quantity':'Position quantity (@qantity)',
      'Current_price':'Current price',
      'Return_rate':'Return rate',
      'Trade_Settings':'Trade Settings',
      'Start':'Start',
      'Currency_Binded':'Currency Binded',
      'All':'All',
      'Circle':'Circle',
      'One-shot':'One-shot',
      'Stop_margin_call':'Stop margin call',
      'Home':'Home',
      'Quantitative':'Quantitative',
      'News':'News',
      'Mine':'Mine',
      'User':'User',
      'Personal_Info':'Personal Info',
      'Nickname':'Nickname',
      'UUID':'UUID',
      'Rank':'Rank',
      'Sponsor':'Sponsor',
      'Avatar':'Avatar',
      'Email':'Email',
      'Location':'Location',
      'Buy':'Buy',
      'Position_amounts':'Position amount',
      'Avg_price':'Avg price',
      'Position_quanty':'Position quantity',
      'PPAL':'Position profit and loss',
      'Estimated_Avg_price':'Estimated Avg price',
      'Estimated_holding_profit_and_loss':'Estimated holding profit and loss',
      'Amount_of_margin_call':'Amount of margin call',
      'Please_enter_purchased_amount':'Please enter purchased amount',
      'Cancel':'Cancel',
      'Enter_amount':'Enter amount',
      'Confirm':'Confirm',
      'Sell':'Sell',
      'Strategy_mode':'Strategy mode',
      'Get_boost':'Get boost',
      'Operation_reminder':'Operation reminder',
      'First_preset_buy_in_price':'First preset buy in price',
      'First_Buy_in_amount':'First buy in amount',
      'Margin_call_limit':'Margin call limit',
      'Take_profit_ratio':'Take profit ratio',
      'Earnings_callback':'Stop Profit Pullback Rate',
      'Margin_call_drop':'Margin call drop',
      'Buy_in_callback':'Buy in callback',
      'Pause':'Pause',
      'Distributed_and_Take_Profit':'Distributed and Take Profit',
      'Margin_Configuration':'Margin Configuration',
      'Multiply_by_in_ration':'Multiply by in ratio',
      'Multiply_by_in_ratio':'Multiply by in ratio',
      'Times':'USDT',
      'Save':'Save',
      'Open_position_doubled':'Open position doubled',
      'Whole_position_take_profit_ratio':'Whole position take profit ratio',
      'Whole_position_take_profit_callback':'Whole position take profit callback',
      'Sub-position_take_profit_callback':'Sub-position take profit callback',
      'Auto_trade_strategy':'Auto trade strategy',
      'firstBuyMessage':'The first buy in amount is calculated according to the currency pair, principle and trade unit',
      'eEzyTradeMessage': 'When the @name robot is operating, '
    'please do not operate the currency account by yourself, and check whether there is a fixed deposit,'
    'freezing and other related settings, so as to avoid abnormal judgments caused by the system and affect your right and interests',
      'No_Records':'No Records',
      'Select_Country':'Select Country',
      'search':'search',
      'Quantity':'Quantity',
      'Price':'Price\t\t',
      'Increase':'Increase\t\t',
      'Search_currency_name':'Search currency name',
      'Select_location':'Select location',

      // Registration
      'read_carefully':'I have carefully read ',
      'service_and_agreement':'the service and agreement',
      'And':' and ',
      'user_privacy_policies':'user privacy policies',
      'Register':'Register',
      'Welcome_you_to_join':'Welcome you to join',
      'Enter_your_name':'Enter your name',
      'Namea':'Name',
      'Please_Enter_E-mail':'Please Enter E-mail',
      'Set_6-12_digit_login_password':'Set 6-12 digit login password',
      'Enter_verification_code':'Enter verification code',
      'Enter_valid_email_id':'Enter valid email id',
      'Send':'Send',
      'Enter_invitation_code':'Enter invitation code',
      'Register_now':'Register now',
      'Free':'Free',
      'Sync_strategy_is_turned_on':'Sync strategy is turned on',
      'people_joined':'@number people joined',
      'Dynamic':'Dynamic',
      'Manage':'Manage....',
      'Discover_circle':'Discover circle',
      'Name_:':'Name:',
      'Username_:':'Username:',
      'Country':'Country:',
      'Invitation_Code':'Invitation Code:',
      'Copied':'Copied',
      'Revenue_Detail':'Revenue Detail',
      'Today_profit':'Today\'s profit(USDT)',
      'Cumulative_profit':'Cumulative profit(USDT)',
      'Data_hour':'Data is counted by every hour',
      'Daily_time':'Daily statistic based on India time @time',
      'I_have_read':'I have read ',
      'the_risk_notice_carefully':'the risk notice carefully',
      'Notification':'Notification',
      'See_the_instructions':'See the instructions',
      'confirm_API_permissions':'1. Please confirm that the API permissions has checked "Enable Spot & Margin Trading"',
      'enter_correct_API':'2. Please enter the correct API, please do not enter special character',
      'IP_group_binding':'IP group binding',
      'For_security':'For security, @title exchange recommends binding the server IP address when creating the API.'
                'For user who need to binding the IP address, click Edit permissions in the upper right corner'
                'after the API is created, and click in the IP address IPs.\n(Recommended) option, click the IP group'
                'to the input box and click OK, after adding, click save in the upper right corner',
      'Ip_copied_to_clipboard':'Ip copied to clipboard',
      'API_Key':'API Key',
      'Please_enter_API_key':'Please enter API key',
      'Secret_Key':'Secret Key',
      'Please_enter_Secret_Key':'Please enter Secret Key',
      'Verification_code':'Verification code',
      'Please_enter_verification':'Please enter verification',
      'verification_code_From':'verification code From @name',
      'verification_otp':'verification code is  @otp, please do not share with other',
      'Bind':'Bind',
      'API_Binded':'API Binded',
      'OTP_requested':'OTP requested',
      'OTP_sent_on':'OTP sent on your registered email address @email',
      'How_To_Use':'How To Use',
      'Configure':'Configure',
      'Invite_Friends':'Invite Friends',
      'Invite_on':'Invite on @name',
      'Use_my_invitation':'Use my invitation code <@code> and get benefits ',
      'Quantity_:':'Quantity: ',
      'Profit_:':'Profit: ',
      'Price_:':'Price: ',
      'Change_:':'Change: ',
      'no_transaction_detail':'There is no transaction detail',
      'OrderStatus':'Order Status',
      'Exchange':'Exchange',
      'Symbol':'Symbol',
      'Time':'Time',
      'Reset':'Reset',
      'Deposit':'Deposit',
      'Currency_:':'Currency:',
      'Change_currency':'Change currency',
      'Amount':'Amount(\$):',
      'Enter_amount_USDT':'Enter amount(USDT)',
      'Net_Amount':'Net Amount',
      'Minimum_value_should_be':'Minimum value should be greater or equals to @amount',
      'Pay_Now':'Pay Now',
      'Have_you_paid':'Have you paid?',
      'Confirm_transaction':'Confirm transaction by sharing hash key',
      'Enter_Hash_key':'Enter Hash key',
      'Hash_Key':'Hash Key',
      'Scan_copy':'Scan or copy the below address to PAY',
      'Scan':'Scan',
      'Address_not_received':'Address not received',
      'Address_copied':'Address copied',
      'Transaction_time_out':'Transaction time out',
      'Transfer':'Transfer',
      'Balance:':'Balance: ',
      'enter_userid':'enter userid',
      'Insufficient_balance':'Insufficient balance',
      'Withdrawal':'Withdrawal',
      'Rate_USDT':'Rate(USDT):',
      'Address:':'Address:',
      'Enter_correct_address':'Enter correct address',
      'Transaction_Detail':'Transaction Detail',
      'Total_asset':'Total asset:',
      'Custom_Amount': 'Custom Amount',
      'Setup_margin_amount': 'Set up margin amount',
      'AIStrategy': 'AI Strategy',
      'ATM_Mode': 'ATM Mode',
      'Copy_Trade': 'Copy Trade',
      'Enter_trans_password': 'Enter transaction password',
      'Conf_trans_password': 'Confirm transaction password',
      'Preset_buy_price': 'Preset buy price',
      'Stop_close_price': 'Stop close price',
      'Reset_transaction_psw': 'Reset transaction password',
      'Change_transaction_psw': 'Change transaction password',
      'Full_details': 'Full details',
      'Direct_qualifications': 'Direct qualifications',
      'Team_qualifications': 'Team qualifications',
      'Pin_activation': 'Pin activation',

    },
    'de_DE': {}
  };
}