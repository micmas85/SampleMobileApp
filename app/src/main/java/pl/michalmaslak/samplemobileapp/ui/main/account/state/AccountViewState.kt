package pl.michalmaslak.samplemobileapp.ui.main.account.state

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import pl.michalmaslak.samplemobileapp.models.AccountProperties

const val ACCOUNT_VIEW_STATE_BUNDLE_KEY = "pl.michalmaslak.samplemobileapp.ui.main.account.state.AccountViewState"

@Parcelize
class AccountViewState (
    var accountProperties: AccountProperties? = null
) : Parcelable