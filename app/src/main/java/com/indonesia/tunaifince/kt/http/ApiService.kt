package com.indonesia.tunaifince.kt.http


import com.indonesia.tunaifince.kt.baseConfig.ApiUrlConfig
import com.indonesia.tunaifince.kt.baseConfig.HttpConfiguration.GET_
import com.indonesia.tunaifince.kt.http.model.*
import okhttp3.RequestBody
import retrofit2.http.*
import java.util.*


interface ApiService {


    /**
     * 域名池
     */
    @GET(ApiUrlConfig.ipPool)
    suspend fun ipPool(): BaseResult<String>


    /**
     * 发送验证码
     *
     * @param mobile
     * @param type (voice/text)
     * @return
     */
    @POST(ApiUrlConfig.code)
    suspend fun sendVerifiyCode(@Body body: RequestBody): BaseResult<SendSmsBody>


    /**
     * 登录
     *
     * @param phone
     * @param code
     * @return
     */
    @POST(ApiUrlConfig.login)
    suspend fun login(@Body body: RequestBody): BaseResult<LoginBody>

    /**
     * 用户信息
     *
     * @return
     */
    @POST(ApiUrlConfig.queryCashUser)
    suspend fun queryCashUser(): BaseResult<QueryCashUserBody>

    /**
     * 退出登录
     *@param version
     * @return
     */
    @GET(ApiUrlConfig.loginOut)
    suspend fun loginOut(@Query(GET_) xxxx: String?): BaseResult<String>


    /**
     * Adjust
     *@param adId
     * @param trackerName
     * @param gps_adid
     *
     * @return
     */
    @POST(ApiUrlConfig.adjust)
    suspend fun adjust(@Body body: RequestBody): BaseResult<Boolean>


    /**
     * 设备信息保存
     *
     * @return
     */
    @POST(ApiUrlConfig.deviceInfo)
    suspend fun deviceInfo(@Body body: RequestBody): BaseResult<Boolean>

    /**
     * 通讯录保存
     *
     * @param jr
     * @return
     */
    @POST(ApiUrlConfig.saveJr)
    suspend fun saveJr(@Body body: RequestBody): BaseResult<Boolean>


    /**
     * APP保存
     *@param ar
     * @return
     */
    @POST(ApiUrlConfig.upAr)
    suspend fun upAr(@Body body: RequestBody): BaseResult<Boolean>


    /**
     * 查询用户反馈
     *@param statusr  状态 0待回复 1已回复 不传默认所有
     */
    @POST(ApiUrlConfig.queryAppUserQuestion)
    suspend fun queryAppUserQuestion(@Body body: RequestBody): BaseResult<List<QueryAppUserQuestionBody>>

    /**
     * 保存用户反馈
     *@param questionSubject
     *@param questionType
     *@param answer
     */
    @POST(ApiUrlConfig.saveAppUserQuestion)
    suspend fun saveAppUserQuestion(@Body body: RequestBody): BaseResult<Boolean>


    /**
     * Firebase推送同步注册token
     *
     * @param registryToken
     * @return
     */
    @POST(ApiUrlConfig.registryTokenInfo)
    @FormUrlEncoded
    suspend fun registryTokenInfo(@Body body: RequestBody): BaseResult<Boolean>


    /**
     * 首页
     *
     * @return
     */
    @GET(ApiUrlConfig.home)
    suspend fun home(): BaseResult<HomeBody>


    /**
     * Config查询config
     *
     * @param configType
     *
     */
    @GET(ApiUrlConfig.config)
    suspend fun config(@Query(GET_) configType: String?): BaseResult<List<ConfigBody>>

    /**
     * Config查询config/one
     * @return
     * /api/v3/common/config/one
     * cNonPfDRRkwV
     *
     * @param configKey
     */
    @GET(ApiUrlConfig.one)
    suspend fun getHomeConfig(@Query(GET_) configKey: String?): BaseResult<OneBody>


    /**
     * 用户认证详情
     * @return
     */
    @POST(ApiUrlConfig.queryUserDetail)
    suspend fun queryUserDetail(): BaseResult<QueryUserDetailBody>


    /**
     * 用户基本信息认证
     *  @param education
     *  @param cashUserContacts
     *  @param maritalStatus
     *  @param relationship
     *  @param contactsName
     *  @param contactsMobile
     *
     * @return
     */
    @POST(ApiUrlConfig.identitycheck)
    suspend fun identityCheck(@Body body: RequestBody): BaseResult<OcrCheckBody>


    /**
     *静默活体检测V3
     *@param  livenessImg
     *@param  channelCode  T011
     * @return
     */
    @POST(ApiUrlConfig.silence)
    suspend fun silence(@Body body: RequestBody): BaseResult<OcrCheckBody>

    /**
     * 身份证信息确认
     * @param idNo
     * @param userName
     * @return
     */
    @POST(ApiUrlConfig.ocrConfirm)
    suspend fun ocrConfirm(@Body body: RequestBody):  BaseResult<Boolean>


    /**
     *身份证信息认证
     *@param idCardPositiveImg
     *
     * @return
     */
    @POST(ApiUrlConfig.ocrCheck)
    suspend fun ocrCheck(@Body body: RequestBody): BaseResult<OcrCheckBody>


    /**
     * 人脸信息认证
     * @param faceImg
     * @return
     */
    @POST(ApiUrlConfig.faceCompare)
    suspend fun faceCompare(@Body body: RequestBody):  BaseResult<OcrCheckBody>

//
//    /**
//     * 获取license
//     *
//     * @return
//     */
//    @GET(ApiUrlConfig.license)
//    suspend fun license(): BaseResult<String>
//
    /**
     * 获取活体识别结果
     *@param livenessId
     * @return
     */
    @POST(ApiUrlConfig.detection)
    suspend fun detection(@Body body: RequestBody):BaseResult<Boolean>


    /**
     * 银行列表
     *
     * @return
     */
    @GET(ApiUrlConfig.bindCardList)
    suspend fun bindCardList(): BaseResult<BankCardListBody>

    /**
     * 绑卡认证
     * "name"
     * "accountNumber"
     * "bankCode"
     * "thirdChannelCode"
     * "code"
     * @return
     */
    @POST(ApiUrlConfig.bind)
    suspend fun bind(
        @Body body: RequestBody
    ): BaseResult<OcrCheckBody>



    /**
     * 弹窗查询
     *
     * /api/v3/common/getPop
     * @param popId
     *
     * @return
     */
    @POST(ApiUrlConfig.getPop)
    suspend fun getPop(@Body body: RequestBody): BaseResult<GetPopBody>


    /**
     * 弹窗确认
     * @param popId
     *
     * @return
     */
    @POST(ApiUrlConfig.confirmPop)
    suspend fun confirmPop(@Body body: RequestBody): BaseResult<Boolean>


    /**
     * 检查是否需要tips
     */
    @POST(ApiUrlConfig.checkTips)
    suspend fun checkTips(): BaseResult<Boolean>


    /**
     * 借款记录
     *@param applyStatus
     * @return
     */
    @POST(ApiUrlConfig.loanOrderRecord)
    suspend fun loanOrderRecord(@Body body: RequestBody): BaseResult<LoanOrderRecordBody>


    /**
     * 借款确认
     * @param cardNo
     * @param pageNum  4
     *  @param version
     *   @param productCodes
     */
    @POST(ApiUrlConfig.confirm)
    suspend fun confirm(@Body body: RequestBody): BaseResult<ConfirmBody>


    /**
     * 还款请求
     *@param repayPlanId
     * @param repayChannel
     * @return
     */
    @POST(ApiUrlConfig.repay)
    suspend fun repay(@Body body: RequestBody): BaseResult<ExtensionRepayBody>


    /**
     * 展期试算
     *@param repayPlanId
     * @param repayChannel
     * @return
     */
    @POST(ApiUrlConfig.trial)
    suspend fun trial(@Body body: RequestBody): BaseResult<TrialBody>


    /**
     * 展期还款
     *@param repayPlanId
     * @param repayChannel
     * @return
     */
    @POST(ApiUrlConfig.extensionRepay)
    suspend fun extensionRepay(@Body body: RequestBody): BaseResult<ExtensionRepayBody>


    /**
     * 借款详情
     *@param applyId
     * @return
     */
    @POST(ApiUrlConfig.loanOrderDetail)
    suspend fun loanOrderDetail(@Body body: RequestBody): BaseResult<LoanOrderDetailBody>


    /**
     * 还款渠道
     *@param repayPlanId
     * @return
     */
    @GET(ApiUrlConfig.channel)
    suspend fun channel(@Query(GET_) repayPlanId: String?): BaseResult<ChannelBody>

    /**
     * 借款检查
     */
    @POST(ApiUrlConfig.check)
    suspend fun check(): BaseResult<CheckBody>

    /**
     * 借款信息
     *@param version
     *@param pageNum
     */
    @POST(ApiUrlConfig.init)
    suspend fun init(@Body body: RequestBody): BaseResult<InitBody>

    /**
     * 活动点击抽奖
     */
    @GET(ApiUrlConfig.lottery)
    suspend fun lottery(): BaseResult<AwardBoby>

    /**
     *  现金奖励活动详情
     */
    @GET(ApiUrlConfig.award)
    suspend fun award(): BaseResult<AwardBoby>


    /**
     * 弹窗列表查询
     * @param queryType :confirm
     *
     * @return
     */
    @GET(ApiUrlConfig.getPopList)
    suspend fun getPopList(@Query(GET_) queryType: String?): BaseResult<List<GetPopBody>>




}