package com.college.di

import com.college.route.*
import org.koin.dsl.module

val routeModule = module {
    factory { AuthRoute(get(), get()) }
    factory { ContactRoute(get()) }
    factory { ReportRoute( get(), get() ) }
    factory { ConsultationRoute(get()) }
    factory { ConsultationTimeRoute(get()) }
}