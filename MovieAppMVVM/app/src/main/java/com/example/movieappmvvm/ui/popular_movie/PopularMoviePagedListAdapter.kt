package com.example.movieappmvvm.ui.popular_movie

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.movieappmvvm.R
import com.example.movieappmvvm.data.api.POSTER_BASE_URL
import com.example.movieappmvvm.data.model.Movie
import com.example.movieappmvvm.data.repository.NetworkState
import com.example.movieappmvvm.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*


class PopularMoviePagedListAdapter(private val context: Context) :
    PagedListAdapter<Movie, ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2
    private var networkState: NetworkState? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (getItemViewType(position) == MOVIE_VIEW_TYPE) {
            (holder as MovieItemViewHolder).bind(getItem(position), context)
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == MOVIE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.movie_list_item, parent, false)
            MovieItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.network_state_item, parent, false)
            MovieItemViewHolder(view)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            MOVIE_VIEW_TYPE
        }
    }


    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : ViewHolder(view) {
        fun bind(movie: Movie?, context: Context) {
            itemView.tvTitle.text = movie?.title
            itemView.tvReleaseDate.text = movie?.releaseDate
            itemView.tvLanguage.text = movie?.language
            itemView.tvPopularity.text = movie?.popularity.toString()
            itemView.tvVote.text = movie?.vote.toString()
            val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
            itemView.let {
                Glide.with(context)
                    .load(moviePosterURL)
                    .into(itemView.imvPoster)
            };

            itemView.layoutMovieItem?.setOnClickListener {
                val intent = Intent(context, SingleMovie::class.java)
                intent.putExtra("id", movie?.id)
                context.startActivity(intent)
            }

        }

    }

    class NetworkStateItemViewHolder(view: View) : ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progressBarItem.visibility = View.VISIBLE
            } else {
                itemView.progressBarItem.visibility = View.GONE
            }
            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.tvErrorMsgItem.visibility = View.VISIBLE
                itemView.tvErrorMsgItem.text = networkState.mgs
            } else if (networkState != null && networkState == NetworkState.ENDOFLIST) {
                itemView.tvErrorMsgItem.visibility = View.VISIBLE
                itemView.tvErrorMsgItem.text = networkState.mgs
            } else {
                itemView.tvErrorMsgItem.visibility = View.GONE
            }
        }
    }
    fun setNetworkState(networkState: NetworkState){
        val previousState =  this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = networkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow){
            if (hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            }else{
                notifyItemRemoved(super.getItemCount())
            }
        }else if (hasExtraRow && previousState != networkState){
            notifyItemChanged(itemCount -1)
        }
    }
}