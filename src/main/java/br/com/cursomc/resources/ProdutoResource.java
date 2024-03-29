package br.com.cursomc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.cursomc.domain.Produto;
import br.com.cursomc.dto.ProdutoDTO;
import br.com.cursomc.services.ProdutoService;
import br.com.cursomc.services.utils.URL;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService produtoService;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Produto> find(@PathVariable Integer id) {

		Produto produto = produtoService.find(id);

		return ResponseEntity.ok().body(produto);
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<ProdutoDTO>> findPage(
			@RequestParam(value="", defaultValue="") String nome, 
			@RequestParam(value="", defaultValue="") String categorias, 
			@RequestParam(value="page", defaultValue="0") Integer page, 
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage, 
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction
			) {
		
		String nomeDecoded = URL.decodeParam(nome);
		
		List<Integer> ids = URL.decodeIntList(categorias);
		
		//Page<Produto> produtos = produtoService.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		Page<Produto> produtos = produtoService.findDistinctByNomeContainingAndCategoriasIn(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		
		Page<ProdutoDTO> categoriaList = produtos.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(categoriaList);
	}

}
