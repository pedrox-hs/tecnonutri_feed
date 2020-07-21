package br.com.pedrosilva.tecnonutri.features.common.view

import br.com.pedrosilva.tecnonutri.features.common.presenter.BasePresenter

interface HasPresenter {
    val presenter: BasePresenter<*>
}