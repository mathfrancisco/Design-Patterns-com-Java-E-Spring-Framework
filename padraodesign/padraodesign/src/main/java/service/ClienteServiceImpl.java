package service;

import model.Cliente;
import model.Endereco;
import repository.ClienteRepository;
import repository.EnderecoRepository;

import java.util.Optional;

public class ClienteServiceImpl extends ClienteService {
    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;
    private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }

    private void salvarClienteComCep(Cliente cliente) {
        String cep = cliente.getEndereco().getCep();
        enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(enderecoRepository.findById(cep).get());
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Cliente cliente, Long id) {
        Optional<Cliente> clienteBusca = clienteRepository.findById(id);
        if (clienteBusca.isPresent()) {
            salvarClienteComCep(cliente);
        }

    }

    @Override
    public void deletar(Long id) {
        clienteRepository.deleteById(id);
    }
}