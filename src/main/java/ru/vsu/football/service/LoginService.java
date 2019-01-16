package ru.vsu.football.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vsu.football.domain.UserRoleId;
import ru.vsu.football.entity.AppUserEntity;
import ru.vsu.football.repository.AppUserEntityRepository;
import ru.vsu.football.repository.UserRoleEntityRepository;
import ru.vsu.football.util.CryptoUtil;
import ru.vsu.football.util.UserContext;


@Service
public class LoginService {
    private AppUserEntityRepository appUserEntityRepository;
    private UserRoleEntityRepository userRoleEntityRepository;

    @Transactional(readOnly = true)
    public void login(String username, String password) {
        AppUserEntity user = appUserEntityRepository.findByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("Пользователя с таким именем не существует");
        }
        if (!user.getPassword().equals(CryptoUtil.toSHA1(password))) {
            throw new IllegalArgumentException("Неверный пароль");
        }
        UserContext.setRole(user.getRole().getId());
    }

    @Transactional
    public void register(String username, String password) {
        AppUserEntity user = appUserEntityRepository.findByUsername(username);
        if (user != null) {
            throw new IllegalArgumentException("Имя пользователя уже существует");
        }
        user = new AppUserEntity();
        user.setUsername(username);
        user.setPassword(CryptoUtil.toSHA1(password));
        user.setRole(userRoleEntityRepository.findOne(UserRoleId.USER));
        appUserEntityRepository.save(user);
        UserContext.setRole(user.getRole().getId());
    }

    @Autowired
    public LoginService(AppUserEntityRepository appUserEntityRepository, UserRoleEntityRepository userRoleEntityRepository) {
        this.appUserEntityRepository = appUserEntityRepository;
        this.userRoleEntityRepository = userRoleEntityRepository;
    }
}
