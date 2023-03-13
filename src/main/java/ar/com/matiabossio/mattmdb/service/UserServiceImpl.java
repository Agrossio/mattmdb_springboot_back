package ar.com.matiabossio.mattmdb.service;

import ar.com.matiabossio.mattmdb.business.domain.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements IUserService{

    @Override
    public List<User> getUsersService() {
        return null;
    }

    @Override
    public Optional<User> getUserByIdService(Integer userId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> createUserService(User userFromRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<User> updateUserService(User userFromRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<User> deleteUserService(Integer userIdFromRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<User> loginUserService(User userFromRequest) {
        return Optional.empty();
    }

    @Override
    public boolean userExists(String emailFromRequest) {
        return false;
    }
}
