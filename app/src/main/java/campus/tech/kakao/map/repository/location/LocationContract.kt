package com.example.kakaotechcampus_2nd_step2_android.repository.location

import android.provider.BaseColumns

object LocationContract : BaseColumns {
    const val TABLE_NAME = "location"
    const val PLACE_NAME = "place_name"
    const val ADDRESS_NAME = "address_name"
    const val CATEGORY_GROUP_NAME = "category_group_name"
}
