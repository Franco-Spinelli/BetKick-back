package com.example.betkickapi.service.user;


import com.example.betkickapi.exception.AccountBalanceException;
import com.example.betkickapi.exception.EntityNotFoundException;
import com.example.betkickapi.model.User;
import com.example.betkickapi.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    @Override
    public Boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public User decrementUserBalance(User user, Double amount) {
        if (user.getAccountBalance() >= amount) {
            user.setAccountBalance(user.getAccountBalance() - amount);
            return userRepository.save(user);
        } else {
            throw new AccountBalanceException("Insufficient funds");
        }
    }

    @Override
    public User incrementUserBalance(User user, Double amount) {
        double currentBalance = user.getAccountBalance();
        if (Double.isFinite(amount) && currentBalance <= Double.MAX_VALUE - amount) {
            user.setAccountBalance(currentBalance + amount);
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Adding the amount would exceed the max possible value");
        }
    }

    @Override
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(User.class, "userId", id));
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
