package hr.algebra.cryptonews.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri
import hr.algebra.cryptonews.dao.CryptoNewsRepository
import hr.algebra.cryptonews.dao.getCryptoNewsRepository
import hr.algebra.cryptonews.model.Item

private const val AUTHORITY = "hr.algebra.cryptonews.api.provider"
private const val PATH = "items"

val CRYPTONEWS_PROVIDER_URI: Uri = Uri.parse("content://$AUTHORITY/$PATH")

private const val ITEMS = 10
private const val ITEM_ID = 20

private val URI_MATCHER = with(UriMatcher(UriMatcher.NO_MATCH)) {
    addURI(AUTHORITY, PATH, ITEMS)
    addURI(AUTHORITY, "$PATH/#", ITEM_ID)
    this
}

class NewsProvider : ContentProvider() {

    private lateinit var cryptoNewsRepository: CryptoNewsRepository
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {

        when (URI_MATCHER.match(uri)) {
            ITEMS -> return cryptoNewsRepository.delete(selection, selectionArgs)

            ITEM_ID -> {
                uri.lastPathSegment?.let {
                    return cryptoNewsRepository.delete("${Item::_id.name}=?", arrayOf(it))
                }
            }

        }
        throw IllegalArgumentException("No such uri")
    }

    override fun getType(uri: Uri): String? {
        TODO("Not needed for this app")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        val id = cryptoNewsRepository.insert(values)
        return ContentUris.withAppendedId(CRYPTONEWS_PROVIDER_URI, id)
    }

    override fun onCreate(): Boolean {
        context?.let { cryptoNewsRepository = getCryptoNewsRepository(it) }
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ) = cryptoNewsRepository.query(projection, selection, selectionArgs, sortOrder)

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {

        when (URI_MATCHER.match(uri)) {
            ITEMS -> return cryptoNewsRepository.update(values, selection, selectionArgs)

            ITEM_ID
            -> {
                uri.lastPathSegment?.let {
                    return cryptoNewsRepository.update(values, "${Item::_id.name}=?", arrayOf(it))
                }
            }
        }
        throw IllegalArgumentException("No such uri")
    }
}