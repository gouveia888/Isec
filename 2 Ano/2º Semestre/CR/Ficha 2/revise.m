function [journey, new_case] = revise(retrieved_cases, new_case, new_price)
    
    retrieved_codes = retrieved_cases{:,1};
    code = str2double('-');
        
    while isnan(code) || fix(code) ~= code || ismember(code, retrieved_codes) == 0
        fprintf('From the retrieved cases, which is the one that better matches your journey?\n');
        code = str2double(input('Journey Code: ','s'));
    end
    
    journey = fix(code);

    %Revise Holiday Time
    lista={'Active', 'Bathing', 'City', 'Education', 'Language', 'Recreation', 'Skiing', 'Wandering'}
    fprintf('\nUpdate your Holiday Type? (y/n)\n');
        option = input('Option: ', 's');

    if option == 'y' || option == 'Y'
        valor = input("New value: ", "s");
        while(ismember(lista,valor)==0)
            valor = input("New value: ", "s");
        end
        new_case.holiday_type = valor;
    end

    %Revise Preço
    if new_price >0
    fprintf('\nUpdate your journey price with the new estimated value? (y/n)\n');
    option = input('Option: ', 's');
     

    if option == 'y' || option == 'Y'
        new_case.price = new_price;
    end
    
    else
            fprintf('\nUpdate Preço? (y/n)\n');
            option = input('Option: ', 's');
    
        if option == 'y' || option == 'Y'
            valor = str2double(input("New value: ", "s"));
            while(isnan(valor)) %devia valiar o intervalo de preço tambem
                  valor = str2double(input("New value: ", "s"));
            end
            new_case.price = valor;
        end
    end

    %Revise numero pessoas
    fprintf('\nUpdate Numero Pessoas? (y/n)\n');
        option = input('Option: ', 's');

    if option == 'y' || option == 'Y'
        valor = str2double(input("New value: ", "s"));
        while(isnan(valor))
              valor = str2double(input("New value: ", "s"));
        end
        new_case.number_persons = valor;
    end


end

