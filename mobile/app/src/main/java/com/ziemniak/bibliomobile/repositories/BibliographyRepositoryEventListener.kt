package com.ziemniak.bibliomobile.repositories

import com.ziemniak.bibliomobile.models.Bibliography

interface BibliographyRepositoryEventListener {
	fun bibliographyFetched(bibliography: Bibliography)
	fun bibliographiesFetched(bibliographies: List<Bibliography>)
}